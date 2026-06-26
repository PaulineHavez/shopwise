package com.shopwise.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentRequest(
        @NotNull LocalDateTime startAt,
        @NotNull LocalDateTime endAt,
        @NotNull UUID serviceId,
        @NotNull UUID merchantId,
        @NotNull UUID customerId
) {}
