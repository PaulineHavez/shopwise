package com.shopwise.service.impl;

import com.shopwise.exception.AnavailableServiceException;
import com.shopwise.model.Appointment;
import com.shopwise.model.enums.AppointmentStatus;
import com.shopwise.repository.AppointmentRepository;
import com.shopwise.service.AppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment createAppointment(Appointment appointment) {

        appointment.setStatus(AppointmentStatus.UPCOMING);

        List<AppointmentStatus> ignoredStatuses = List.of(
                AppointmentStatus.CANCELLED,
                AppointmentStatus.HONOURED
        );
        boolean isOverlapping = appointmentRepository.existsOverlappingAppointment(
                appointment.getServiceId(),
                appointment.getStartAt(),
                appointment.getEndAt(),
                ignoredStatuses
        );

        if (isOverlapping) {
            throw new AnavailableServiceException();
        }

        return appointmentRepository.save(appointment);
    }
}
