package com.shopwise.dto;

import com.shopwise.model.Appointment;
import com.shopwise.model.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID appointmentId,
        LocalDateTime startAt,
        LocalDateTime endAt,
        AppointmentStatus status,
        Short earnedPoints,
        UUID serviceId,
        UUID merchantId,
        UUID customerId,
        String customerEmail
) {
    public static AppointmentResponse from (Appointment appointment) {
        return new AppointmentResponse(
                appointment.getAppointmentId(),
                appointment.getStartAt(),
                appointment.getEndAt(),
                appointment.getStatus(),
                appointment.getEarnedPoints(),
                appointment.getServiceId(),
                appointment.getMerchantId(),
                appointment.getCustomerId(),
                appointment.getCustomer() != null ? appointment.getCustomer().getEmail() : null
        );
    }
}