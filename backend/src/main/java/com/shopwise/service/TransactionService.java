package com.shopwise.service;
import com.shopwise.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    List<Transaction>  getTransactionsByCustomerId(UUID customerId);
}
