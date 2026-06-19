package com.shopwise.controller;

import com.shopwise.model.Transaction;
import com.shopwise.service.TransactionService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
@Validated
public class TransactionController {

    private TransactionService transactionService;

    @GetMapping("/{customerId}")
    @ResponseStatus(code= HttpStatus.OK)
    public List<Transaction> getServiceByName(@PathVariable UUID customerId){
        return transactionService.getTransactionsByCustomerId(customerId);
    }
}