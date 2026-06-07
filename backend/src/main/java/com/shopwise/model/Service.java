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
@Table(name = "SERVICE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "service_id", updatable = false, nullable = false)
    private UUID serviceId;

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "merchant_id", nullable = false)
    @NotNull
    private UUID merchantId;
}
