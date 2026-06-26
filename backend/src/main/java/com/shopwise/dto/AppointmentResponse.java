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
    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getAppointmentId(),
                appointment.getStartAt(),
                appointment.getEndAt(),
                appointment.getStatus(),
                appointment.getEarnedPoints(),
                appointment.getService() != null ? appointment.getService().getServiceId() : null,
                appointment.getMerchant() != null ? appointment.getMerchant().getMerchantId() : null,
                appointment.getCustomer() != null ? appointment.getCustomer().getCustomerId() : null,
                appointment.getCustomer() != null ? appointment.getCustomer().getEmail() : null
        );
    }
}
