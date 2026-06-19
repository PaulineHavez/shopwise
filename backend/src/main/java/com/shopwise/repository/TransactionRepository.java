package com.shopwise.repository;

import com.shopwise.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> getTransactionsByCustomerId(UUID customerId);

    @Query("SELECT SUM(t.earnedPoints) FROM Transaction t WHERE t.customerId = :customerId")
    Integer getEarnedPointsByCustomerId(@Param("customerId") UUID customerId);
}
