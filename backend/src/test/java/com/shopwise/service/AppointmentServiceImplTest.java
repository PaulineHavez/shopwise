package com.shopwise.service;

import com.shopwise.dto.AppointmentRequest;
import com.shopwise.exception.AnavailableServiceException;
import com.shopwise.exception.AppointmentCompletedException;
import com.shopwise.exception.AppointmentNotFoundException;
import com.shopwise.exception.MerchantNotFoundException;
import com.shopwise.model.Appointment;
import com.shopwise.model.Customer;
import com.shopwise.model.Merchant;
import com.shopwise.model.enums.AppointmentStatus;
import com.shopwise.repository.AppointmentRepository;
import com.shopwise.repository.CustomerRepository;
import com.shopwise.repository.MerchantRepository;
import com.shopwise.repository.ServiceRepository;
import com.shopwise.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AppointmentServiceImpl service;

    private final UUID appointmentId = UUID.fromString("00000000-0000-0000-0000-000000000010");
    private final UUID merchantId    = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private final UUID customerId    = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID serviceId     = UUID.fromString("00000000-0000-0000-0000-000000000003");

    private Merchant buildMerchant(Short points) {
        return new Merchant(merchantId, "Shop", "0601020304", "shop@example.com",
                "1 rue de la Paix", "12345678901234", "hashed", points);
    }

    private com.shopwise.model.Service buildService() {
        return new com.shopwise.model.Service(serviceId, "Coupe", buildMerchant(null));
    }

    private Customer buildCustomer() {
        return new Customer(customerId, "Alice", "0601020304", "alice@example.com", buildMerchant(null), null);
    }

    private Appointment buildAppointment(AppointmentStatus status) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(appointmentId);
        appointment.setStartAt(LocalDateTime.of(2026, 6, 20, 10, 0));
        appointment.setEndAt(LocalDateTime.of(2026, 6, 20, 11, 0));
        appointment.setStatus(status);
        appointment.setService(buildService());
        appointment.setMerchant(buildMerchant(null));
        appointment.setCustomer(buildCustomer());
        return appointment;
    }

    private AppointmentRequest buildRequest() {
        return new AppointmentRequest(
                LocalDateTime.of(2026, 6, 20, 10, 0),
                LocalDateTime.of(2026, 6, 20, 11, 0),
                serviceId,
                merchantId,
                customerId
        );
    }

    // createAppointment

    @Test
    void createAppointment_overlapping_throwsAnavailableServiceException() {
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(buildService()));
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(buildMerchant(null)));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(buildCustomer()));
        when(appointmentRepository.existsOverlappingAppointment(
                eq(serviceId), any(), any(), any())).thenReturn(true);

        assertThatThrownBy(() -> service.createAppointment(buildRequest()))
                .isInstanceOf(AnavailableServiceException.class);

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void createAppointment_noOverlap_setsStatusUpcomingAndSaves() {
        Appointment saved = buildAppointment(AppointmentStatus.UPCOMING);
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(buildService()));
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(buildMerchant(null)));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(buildCustomer()));
        when(appointmentRepository.existsOverlappingAppointment(
                eq(serviceId), any(), any(), any())).thenReturn(false);
        when(appointmentRepository.save(any())).thenReturn(saved);

        Appointment result = service.createAppointment(buildRequest());

        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.UPCOMING);
        verify(appointmentRepository).save(any());
    }

    // getAppointments

    @Test
    void getAppointments_delegatesToRepositoryWithSpec() {
        Appointment appointment = buildAppointment(AppointmentStatus.UPCOMING);
        when(appointmentRepository.findAll(any(Specification.class))).thenReturn(List.of(appointment));

        List<Appointment> result = service.getAppointments(merchantId, LocalDate.of(2026, 6, 20),
                AppointmentStatus.UPCOMING, "alice@example.com");

        assertThat(result).hasSize(1);
    }

    @Test
    void getAppointments_nullFilters_returnsAllFromSpec() {
        when(appointmentRepository.findAll(any(Specification.class))).thenReturn(List.of());

        List<Appointment> result = service.getAppointments(null, null, null, null);

        assertThat(result).isEmpty();
    }

    // editAppointment

    @Test
    void editAppointment_notFound_throwsAppointmentNotFoundException() {
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.editAppointment(appointmentId, AppointmentStatus.CANCELLED))
                .isInstanceOf(AppointmentNotFoundException.class);
    }

    @Test
    void editAppointment_alreadyCompleted_throwsAppointmentCompletedException() {
        Appointment appointment = buildAppointment(AppointmentStatus.COMPLETED);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        assertThatThrownBy(() -> service.editAppointment(appointmentId, AppointmentStatus.CANCELLED))
                .isInstanceOf(AppointmentCompletedException.class);
    }

    @Test
    void editAppointment_cancelUpcoming_savesWithNewStatus() {
        Appointment appointment = buildAppointment(AppointmentStatus.UPCOMING);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment result = service.editAppointment(appointmentId, AppointmentStatus.CANCELLED);

        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
        assertThat(result.getEarnedPoints()).isNull();
    }

    @Test
    void editAppointment_completeWithMerchantPoints_setsEarnedPoints() {
        Appointment appointment = buildAppointment(AppointmentStatus.UPCOMING);
        appointment.setMerchant(buildMerchant((short) 10));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment result = service.editAppointment(appointmentId, AppointmentStatus.COMPLETED);

        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
        assertThat(result.getEarnedPoints()).isEqualTo((short) 10);
    }

    @Test
    void editAppointment_completeWithNullMerchantPoints_doesNotSetEarnedPoints() {
        Appointment appointment = buildAppointment(AppointmentStatus.UPCOMING);
        appointment.setMerchant(buildMerchant(null));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment result = service.editAppointment(appointmentId, AppointmentStatus.COMPLETED);

        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
        assertThat(result.getEarnedPoints()).isNull();
    }

    @Test
    void editAppointment_completeMerchantNotFound_throwsMerchantNotFoundException() {
        Appointment appointment = buildAppointment(AppointmentStatus.UPCOMING);
        appointment.setMerchant(null);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        assertThatThrownBy(() -> service.editAppointment(appointmentId, AppointmentStatus.COMPLETED))
                .isInstanceOf(MerchantNotFoundException.class);
    }
}
