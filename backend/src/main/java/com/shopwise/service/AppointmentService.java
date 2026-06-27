package com.shopwise.service;

import com.shopwise.dto.AppointmentRequest;
import com.shopwise.model.Appointment;
import com.shopwise.model.enums.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public interface AppointmentService {

    Appointment createAppointment(AppointmentRequest request);

    List<Appointment> getAppointments(UUID merchantId, LocalDate date, AppointmentStatus status, String email);

    Appointment editAppointment(UUID appointmentId, AppointmentStatus status);
}
