package com.shopwise.service;

import com.shopwise.model.Appointment;
import org.springframework.stereotype.Service;

public interface AppointmentService {

    Appointment createAppointment(Appointment appointment);
}
