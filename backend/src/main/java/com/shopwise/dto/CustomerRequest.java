package com.shopwise.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CustomerRequest(
        @NotNull String name,
        @NotNull String phoneNumber,
        @NotNull String email,
        @NotNull UUID merchantId
) {}
