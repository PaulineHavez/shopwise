package com.shopwise.dto;

import java.util.UUID;

public record CustomerLoginResponse(
        UUID customerId,
        String name,
        String phoneNumber,
        String email) {}