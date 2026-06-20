package com.shopwise.service;

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

    @Test
    void getTransactionsByCustomerId_returnsTransactionList() {
        Transaction tx = new Transaction(UUID.randomUUID(), LocalDateTime.now(),
                TransactionStatus.COMPLETED, (short) 10, serviceId, merchantId, customerId);
        when(transactionRepository.getTransactionsByCustomerId(customerId)).thenReturn(List.of(tx));

        List<Transaction> result = service.getTransactionsByCustomerId(customerId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomerId()).isEqualTo(customerId);
    }

    @Test
    void getTransactionsByCustomerId_noTransactions_returnsEmptyList() {
        when(transactionRepository.getTransactionsByCustomerId(customerId)).thenReturn(List.of());

        List<Transaction> result = service.getTransactionsByCustomerId(customerId);

        assertThat(result).isEmpty();
    }
}
