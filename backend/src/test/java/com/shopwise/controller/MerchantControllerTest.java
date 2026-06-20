package com.shopwise.controller;

import tools.jackson.databind.ObjectMapper;
import com.shopwise.config.SecurityConfig;
import com.shopwise.dto.LoginRequest;
import com.shopwise.dto.MerchantLoginResponse;
import com.shopwise.exception.BadCredentialsException;
import com.shopwise.exception.MerchantNotFoundException;
import com.shopwise.model.Merchant;
import com.shopwise.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MerchantController.class)
@Import(SecurityConfig.class)
class MerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MerchantService merchantService;

    private final UUID merchantId = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private Merchant buildMerchant() {
        return new Merchant(merchantId, "ShopCo", "0601020304", "shop@example.com",
                "1 rue de la Paix, Paris", "12345678901234", "hashed_password", (short) 10);
    }

    @Test
    void getMerchantById_existingId_returns200() throws Exception {
        when(merchantService.getMerchantById(merchantId)).thenReturn(buildMerchant());

        mockMvc.perform(get("/api/merchants/{id}", merchantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ShopCo"))
                .andExpect(jsonPath("$.email").value("shop@example.com"));
    }

    @Test
    void getMerchantById_unknownId_returns404() throws Exception {
        when(merchantService.getMerchantById(merchantId)).thenThrow(new MerchantNotFoundException());

        mockMvc.perform(get("/api/merchants/{id}", merchantId))
                .andExpect(status().isNotFound());
    }

    @Test
    void login_validCredentials_returns200() throws Exception {
        MerchantLoginResponse response = new MerchantLoginResponse(merchantId, "ShopCo", "shop@example.com");
        when(merchantService.login(any(LoginRequest.class))).thenReturn(response);

        LoginRequest request = new LoginRequest("shop@example.com", "secret");

        mockMvc.perform(post("/api/merchants/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.merchantId").value(merchantId.toString()))
                .andExpect(jsonPath("$.name").value("ShopCo"))
                .andExpect(jsonPath("$.email").value("shop@example.com"));
    }

    @Test
    void login_wrongPassword_returns401() throws Exception {
        when(merchantService.login(any(LoginRequest.class))).thenThrow(new BadCredentialsException());

        LoginRequest request = new LoginRequest("shop@example.com", "wrong");

        mockMvc.perform(post("/api/merchants/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_unknownEmail_returns404() throws Exception {
        when(merchantService.login(any(LoginRequest.class))).thenThrow(new MerchantNotFoundException());

        LoginRequest request = new LoginRequest("unknown@example.com", "secret");

        mockMvc.perform(post("/api/merchants/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
