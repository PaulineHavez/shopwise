package com.shopwise.service;

import com.shopwise.model.Customer;
import com.shopwise.model.Merchant;
import com.shopwise.model.Transaction;
import com.shopwise.model.enums.TransactionStatus;
import com.shopwise.repository.TransactionRepository;
import com.shopwise.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl service;

    private final UUID customerId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID merchantId = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private final UUID serviceId  = UUID.fromString("00000000-0000-0000-0000-000000000003");

    private Customer buildCustomer() {
        Merchant merchant = new Merchant(merchantId, "Shop", "0600000000", "shop@example.com",
                "1 rue de la Paix", "12345678901234", "hashed", null);
        return new Customer(customerId, "Alice", "0601020304", "alice@example.com", merchant, null);
    }

    @Test
    void getTransactionsByCustomerId_returnsTransactionList() {
        Customer customer = buildCustomer();
        Transaction tx = new Transaction(UUID.randomUUID(), LocalDateTime.now(),
                TransactionStatus.COMPLETED, (short) 10, null, null, customer);
        when(transactionRepository.getTransactionsByCustomerId(customerId)).thenReturn(List.of(tx));

        List<Transaction> result = service.getTransactionsByCustomerId(customerId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomer().getCustomerId()).isEqualTo(customerId);
    }

    @Test
    void getTransactionsByCustomerId_noTransactions_returnsEmptyList() {
        when(transactionRepository.getTransactionsByCustomerId(customerId)).thenReturn(List.of());

        List<Transaction> result = service.getTransactionsByCustomerId(customerId);

        assertThat(result).isEmpty();
    }
}
