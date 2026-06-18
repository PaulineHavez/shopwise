package com.shopwise.dto;

import java.util.UUID;

public record MerchantLoginResponse(
        UUID merchantId,
        String name,
        String email) {}