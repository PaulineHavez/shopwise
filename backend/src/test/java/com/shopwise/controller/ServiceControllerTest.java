package com.shopwise.controller;

import com.shopwise.config.SecurityConfig;
import com.shopwise.exception.ServiceNotFoundException;
import com.shopwise.model.Merchant;
import com.shopwise.model.Service;
import com.shopwise.service.ServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServiceController.class)
@Import(SecurityConfig.class)
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceService serviceService;

    private final UUID serviceId  = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID merchantId = UUID.fromString("00000000-0000-0000-0000-000000000002");

    private Merchant buildMerchant() {
        return new Merchant(merchantId, "Shop", "0600000000", "shop@example.com",
                "1 rue de la Paix", "12345678901234", "hashed", null);
    }

    @Test
    void getServiceByName_existingName_returns200() throws Exception {
        Service service = new Service(serviceId, "Coupe", buildMerchant());
        when(serviceService.getServiceByName("Coupe")).thenReturn(service);

        mockMvc.perform(get("/api/services/{name}", "Coupe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Coupe"))
                .andExpect(jsonPath("$.serviceId").value(serviceId.toString()));
    }

    @Test
    void getServiceByName_unknownName_returns404() throws Exception {
        when(serviceService.getServiceByName("Inconnu")).thenThrow(new ServiceNotFoundException());

        mockMvc.perform(get("/api/services/{name}", "Inconnu"))
                .andExpect(status().isNotFound());
    }
}
