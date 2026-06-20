package com.shopwise.controller;

import tools.jackson.databind.ObjectMapper;
import com.shopwise.config.SecurityConfig;
import com.shopwise.dto.CustomerLoginResponse;
import com.shopwise.dto.LoginRequest;
import com.shopwise.dto.RegisterRequest;
import com.shopwise.exception.CustomerNotFoundException;
import com.shopwise.model.Customer;
import com.shopwise.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Import(SecurityConfig.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    private final UUID customerId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID merchantId = UUID.fromString("00000000-0000-0000-0000-000000000002");

    private Customer buildCustomer() {
        return new Customer(customerId, "Alice", "0601020304", "alice@example.com", merchantId, null);
    }

    @Test
    void getCustomers_returns200WithList() throws Exception {
        when(customerService.getCustomers()).thenReturn(List.of(buildCustomer()));

        mockMvc.perform(get("/api/customers/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"));
    }

    @Test
    void getCustomer_existingId_returns200() throws Exception {
        when(customerService.getCustomer(customerId)).thenReturn(buildCustomer());

        mockMvc.perform(get("/api/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void getCustomer_unknownId_returns404() throws Exception {
        when(customerService.getCustomer(customerId)).thenThrow(new CustomerNotFoundException());

        mockMvc.perform(get("/api/customers/{id}", customerId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer_validBody_returns201() throws Exception {
        Customer customer = buildCustomer();
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/api/customers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void createCustomer_missingRequiredField_returns400() throws Exception {
        String invalidBody = "{\"name\": \"Alice\"}";

        mockMvc.perform(post("/api/customers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCustomer_existingId_returns204() throws Exception {
        doNothing().when(customerService).deleteCustomer(customerId);

        mockMvc.perform(delete("/api/customers/{id}", customerId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCustomer_unknownId_returns404() throws Exception {
        doThrow(new CustomerNotFoundException()).when(customerService).deleteCustomer(customerId);

        mockMvc.perform(delete("/api/customers/{id}", customerId))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomer_existingId_returns200() throws Exception {
        Customer updated = new Customer(customerId, "Alice Updated", "0601020304", "alice@example.com", merchantId, null);
        when(customerService.updateCustomer(eq(customerId), any(Customer.class))).thenReturn(updated);

        mockMvc.perform(put("/api/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Updated"));
    }

    @Test
    void getByEmail_existingEmail_returns200() throws Exception {
        when(customerService.getCustomerByEmail("alice@example.com")).thenReturn(buildCustomer());

        mockMvc.perform(get("/api/customers/email/{email}", "alice@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void login_validCredentials_returns200() throws Exception {
        CustomerLoginResponse response = new CustomerLoginResponse(customerId, "Alice", "0601020304", "alice@example.com");
        when(customerService.login(any(LoginRequest.class))).thenReturn(response);

        LoginRequest request = new LoginRequest("alice@example.com", "secret");

        mockMvc.perform(post("/api/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId.toString()))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void login_wrongPassword_returns401() throws Exception {
        when(customerService.login(any(LoginRequest.class)))
                .thenThrow(new com.shopwise.exception.BadCredentialsException());

        LoginRequest request = new LoginRequest("alice@example.com", "wrong");

        mockMvc.perform(post("/api/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register_newAccount_returns200WithTrue() throws Exception {
        when(customerService.register(any(RegisterRequest.class))).thenReturn(true);

        RegisterRequest request = new RegisterRequest("alice@example.com", "newPassword");

        mockMvc.perform(put("/api/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getCustomerEarnedPoints_returns200WithTotal() throws Exception {
        when(customerService.getCustomerEarnedPoints(customerId)).thenReturn((short) 150);

        mockMvc.perform(get("/api/customers/{id}/earnedPoints/", customerId))
                .andExpect(status().isOk())
                .andExpect(content().string("150"));
    }
}
