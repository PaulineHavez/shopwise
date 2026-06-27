package com.shopwise.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

    @Entity
    @Table(name = "CUSTOMER")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Customer {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "customer_id", updatable = false, nullable = false)
        private UUID customerId;

        @Column(name = "name", nullable = false)
        @NotNull
        private String name;

        @Column(name = "phone_number", nullable = false, unique = true, length = 15)
        @NotNull
        private String phoneNumber;

        @Column(name = "email", nullable = false, unique = true)
        @NotNull
        private String email;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "merchant_id", nullable = false)
        @JsonIgnore
        private Merchant merchant;

        @Column(name = "hash_password")
        private String password;
    }
