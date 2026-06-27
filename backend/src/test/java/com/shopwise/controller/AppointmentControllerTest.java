package com.shopwise.controller;

import tools.jackson.databind.ObjectMapper;
import com.shopwise.config.SecurityConfig;
import com.shopwise.dto.AppointmentRequest;
import com.shopwise.model.Appointment;
import com.shopwise.model.Customer;
import com.shopwise.model.Merchant;
import com.shopwise.model.enums.AppointmentStatus;
import com.shopwise.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
@Import(SecurityConfig.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppointmentService appointmentService;

    private final UUID appointmentId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID merchantId    = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private final UUID customerId    = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private final UUID serviceId     = UUID.fromString("00000000-0000-0000-0000-000000000004");

    private Appointment buildAppointment(AppointmentStatus status) {
        Merchant merchant = new Merchant(merchantId, "ShopCo", "0601020304", "shop@example.com",
                "1 rue de la Paix", "12345678901234", "hashed", null);
        com.shopwise.model.Service service = new com.shopwise.model.Service(serviceId, "Coupe", merchant);
        Customer customer = new Customer(customerId, "Alice", "0601020304", "alice@example.com", merchant, null);

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(appointmentId);
        appointment.setStartAt(LocalDateTime.of(2025, 6, 20, 10, 0));
        appointment.setEndAt(LocalDateTime.of(2025, 6, 20, 11, 0));
        appointment.setStatus(status);
        appointment.setEarnedPoints((short) 10);
        appointment.setService(service);
        appointment.setMerchant(merchant);
        appointment.setCustomer(customer);
        return appointment;
    }

    private AppointmentRequest buildRequest() {
        return new AppointmentRequest(
                LocalDateTime.of(2025, 6, 20, 10, 0),
                LocalDateTime.of(2025, 6, 20, 11, 0),
                serviceId,
                merchantId,
                customerId
        );
    }

    @Test
    void createAppointment_validBody_returns201() throws Exception {
        Appointment appointment = buildAppointment(AppointmentStatus.UPCOMING);
        when(appointmentService.createAppointment(any(AppointmentRequest.class))).thenReturn(appointment);

        mockMvc.perform(post("/api/appointments/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.appointmentId").value(appointmentId.toString()))
                .andExpect(jsonPath("$.status").value("UPCOMING"));
    }

    @Test
    void createAppointment_missingRequiredField_returns400() throws Exception {
        String invalidBody = "{\"status\": \"UPCOMING\"}";

        mockMvc.perform(post("/api/appointments/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAppointments_byMerchantId_returns200WithList() throws Exception {
        when(appointmentService.getAppointments(eq(merchantId), any(), any(), any()))
                .thenReturn(List.of(buildAppointment(AppointmentStatus.UPCOMING)));

        mockMvc.perform(get("/api/appointments/{merchantId}", merchantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentId").value(appointmentId.toString()))
                .andExpect(jsonPath("$[0].merchantId").value(merchantId.toString()));
    }

    @Test
    void getAppointments_withDateFilter_returns200() throws Exception {
        when(appointmentService.getAppointments(eq(merchantId), any(), any(), any()))
                .thenReturn(List.of(buildAppointment(AppointmentStatus.UPCOMING)));

        mockMvc.perform(get("/api/appointments/{merchantId}", merchantId)
                        .param("date", "2025-06-20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startAt").value("2025-06-20T10:00:00"))
                .andExpect(jsonPath("$[0].endAt").value("2025-06-20T11:00:00"));
    }

    @Test
    void getAppointments_withStatusFilter_returns200() throws Exception {
        when(appointmentService.getAppointments(eq(merchantId), any(), eq(AppointmentStatus.COMPLETED), any()))
                .thenReturn(List.of(buildAppointment(AppointmentStatus.COMPLETED)));

        mockMvc.perform(get("/api/appointments/{merchantId}", merchantId)
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("COMPLETED"));
    }

    @Test
    void updateAppointmentStatus_returns200() throws Exception {
        when(appointmentService.getAppointments(eq(merchantId), any(), any(), any()))
                .thenReturn(List.of(buildAppointment(AppointmentStatus.UPCOMING)));

        mockMvc.perform(get("/api/appointments/{merchantId}", merchantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("UPCOMING"));

        Appointment updated = buildAppointment(AppointmentStatus.COMPLETED);
        when(appointmentService.editAppointment(eq(appointmentId), eq(AppointmentStatus.COMPLETED)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/appointments/{id}", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "COMPLETED"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}
