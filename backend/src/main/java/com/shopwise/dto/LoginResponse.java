package com.shopwise.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record LoginResponse(
        UUID merchantId,
        String name,
        String email) {}