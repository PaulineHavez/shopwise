package com.shopwise.service.impl;

import com.shopwise.exception.AnavailableServiceException;
import com.shopwise.model.Appointment;
import com.shopwise.model.enums.AppointmentStatus;
import com.shopwise.repository.AppointmentRepository;
import com.shopwise.service.AppointmentService;
import com.shopwise.specification.AppointmentSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
                AppointmentStatus.COMPLETED
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

    @Override
    public List<Appointment> getAppointments(UUID merchantId, LocalDate date, AppointmentStatus status, String email) {
        Specification<Appointment> spec = AppointmentSpecification.filterBy(merchantId, date, status, email);
        return appointmentRepository.findAll(spec);
    }
}
