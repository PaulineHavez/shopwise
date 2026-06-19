package com.shopwise.model;

import com.shopwise.model.enums.TransactionStatus;
import jakarta.persistence.*;
        import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TRANSACTION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id", updatable = false, nullable = false)
    private UUID transactionId;

    @Column(name = "transaction_date", nullable = false)
    @NotNull
    private LocalDateTime transaction_date;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "earned_points")
    private Short earnedPoints;

    @Column(name = "service_id", nullable = false)
    @NotNull
    private UUID serviceId;;

    @Column(name = "merchant_id", nullable = false)
    @NotNull
    private UUID merchantId;

    @Column(name = "customer_id", nullable = false)
    @NotNull
    private UUID customerId;
}
