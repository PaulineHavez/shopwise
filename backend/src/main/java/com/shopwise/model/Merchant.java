package com.shopwise.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Entity
@Table(name = "MERCHANT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "merchant_id", updatable = false, nullable = false)
    private UUID merchantId;

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "phone_number", nullable = false, unique = true, length = 15)
    @NotNull
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    @NotNull
    private String email;

    @Column(name = "headquarters_address", nullable = false, unique = true, length = 255)
    @NotNull
    private String headquarters_address;

    @Column(name = "siret_number", nullable = false, unique = true, length = 14)
    @NotNull
    private String siret_number;

    @Column(name = "hash_password", nullable = false)
    @NotNull
    private String password;
}
