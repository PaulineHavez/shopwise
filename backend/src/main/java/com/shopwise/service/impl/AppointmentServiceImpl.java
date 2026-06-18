package com.shopwise.service.impl;

import com.shopwise.exception.AnavailableServiceException;
import com.shopwise.exception.AppointmentCompletedException;
import com.shopwise.exception.AppointmentNotFoundException;
import com.shopwise.exception.MerchantNotFoundException;
import com.shopwise.model.Appointment;
import com.shopwise.model.Merchant;
import com.shopwise.model.enums.AppointmentStatus;
import com.shopwise.repository.AppointmentRepository;
import com.shopwise.repository.MerchantRepository;
import com.shopwise.service.AppointmentService;
import com.shopwise.specification.AppointmentSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final MerchantRepository merchantRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, MerchantRepository merchantRepository) {
        this.appointmentRepository = appointmentRepository;
        this.merchantRepository = merchantRepository;
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

    @Override
    @Transactional
    public Appointment editAppointment(UUID appointmentId, AppointmentStatus status) {

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(AppointmentNotFoundException::new);

        if(appointment.getStatus() ==AppointmentStatus.COMPLETED){
            throw new AppointmentCompletedException();
        }

        appointment.setStatus(status);

        if(status == AppointmentStatus.COMPLETED) {

            Merchant merchant = merchantRepository
                    .findById(appointment.getMerchantId())
                    .orElseThrow(MerchantNotFoundException::new);

            Number points = merchant.getAutomatic_earned_points();

            if(points != null) {
                appointment.setEarnedPoints(
                        points.shortValue()
                );
            }

        }

        return appointmentRepository.save(appointment);
    }
}
