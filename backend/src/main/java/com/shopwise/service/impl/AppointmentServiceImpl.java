package com.shopwise.service.impl;

import com.shopwise.dto.AppointmentRequest;
import com.shopwise.exception.AnavailableServiceException;
import com.shopwise.exception.AppointmentCompletedException;
import com.shopwise.exception.AppointmentNotFoundException;
import com.shopwise.exception.CustomerNotFoundException;
import com.shopwise.exception.MerchantNotFoundException;
import com.shopwise.exception.ServiceNotFoundException;
import com.shopwise.model.Appointment;
import com.shopwise.model.Customer;
import com.shopwise.model.Merchant;
import com.shopwise.model.enums.AppointmentStatus;
import com.shopwise.repository.AppointmentRepository;
import com.shopwise.repository.CustomerRepository;
import com.shopwise.repository.MerchantRepository;
import com.shopwise.repository.ServiceRepository;
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
    private final ServiceRepository serviceRepository;
    private final MerchantRepository merchantRepository;
    private final CustomerRepository customerRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  ServiceRepository serviceRepository,
                                  MerchantRepository merchantRepository,
                                  CustomerRepository customerRepository) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.merchantRepository = merchantRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Appointment createAppointment(AppointmentRequest request) {
        com.shopwise.model.Service serviceEntity = serviceRepository.findById(request.serviceId())
                .orElseThrow(ServiceNotFoundException::new);
        Merchant merchant = merchantRepository.findById(request.merchantId())
                .orElseThrow(MerchantNotFoundException::new);
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(CustomerNotFoundException::new);

        List<AppointmentStatus> ignoredStatuses = List.of(
                AppointmentStatus.CANCELLED,
                AppointmentStatus.COMPLETED
        );
        boolean isOverlapping = appointmentRepository.existsOverlappingAppointment(
                serviceEntity.getServiceId(),
                request.startAt(),
                request.endAt(),
                ignoredStatuses
        );

        if (isOverlapping) {
            throw new AnavailableServiceException();
        }

        Appointment appointment = new Appointment();
        appointment.setStartAt(request.startAt());
        appointment.setEndAt(request.endAt());
        appointment.setService(serviceEntity);
        appointment.setMerchant(merchant);
        appointment.setCustomer(customer);
        appointment.setStatus(AppointmentStatus.UPCOMING);

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

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppointmentCompletedException();
        }

        appointment.setStatus(status);

        if (status == AppointmentStatus.COMPLETED) {
            Merchant merchant = appointment.getMerchant();
            if (merchant == null) throw new MerchantNotFoundException();

            Number points = merchant.getAutomaticEarnedPoints();
            if (points != null) {
                appointment.setEarnedPoints(points.shortValue());
            }
        }

        return appointmentRepository.save(appointment);
    }
}
