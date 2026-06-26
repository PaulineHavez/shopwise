package com.shopwise.controller;

import com.shopwise.config.SecurityConfig;
import com.shopwise.model.Customer;
import com.shopwise.model.Merchant;
import com.shopwise.model.Transaction;
import com.shopwise.model.enums.TransactionStatus;
import com.shopwise.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@Import(SecurityConfig.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    private final UUID customerId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID merchantId = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private final UUID txId       = UUID.fromString("00000000-0000-0000-0000-000000000004");

    private Customer buildCustomer() {
        Merchant merchant = new Merchant(merchantId, "Shop", "0600000000", "shop@example.com",
                "1 rue de la Paix", "12345678901234", "hashed", null);
        return new Customer(customerId, "Alice", "0601020304", "alice@example.com", merchant, null);
    }

    @Test
    void getTransactionsByCustomerId_returns200WithList() throws Exception {
        Transaction tx = new Transaction(txId, LocalDateTime.of(2025, 1, 10, 9, 0),
                TransactionStatus.COMPLETED, (short) 20, null, null, buildCustomer());
        when(transactionService.getTransactionsByCustomerId(customerId)).thenReturn(List.of(tx));

        mockMvc.perform(get("/api/transactions/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value(txId.toString()))
                .andExpect(jsonPath("$[0].status").value("COMPLETED"))
                .andExpect(jsonPath("$[0].earnedPoints").value(20));
    }

    @Test
    void getTransactionsByCustomerId_noTransactions_returns200WithEmptyList() throws Exception {
        when(transactionService.getTransactionsByCustomerId(customerId)).thenReturn(List.of());

        mockMvc.perform(get("/api/transactions/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
