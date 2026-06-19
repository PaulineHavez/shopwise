package com.shopwise.service.impl;

import java.util.List;
import java.util.UUID;

import com.shopwise.model.Transaction;
import com.shopwise.repository.TransactionRepository;
import com.shopwise.service.TransactionService;

@org.springframework.stereotype.Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getTransactionsByCustomerId(UUID customerId) {
        return transactionRepository.getTransactionsByCustomerId(customerId);
    }

}
