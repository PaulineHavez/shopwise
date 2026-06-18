package com.shopwise.controller;

import com.shopwise.dto.AppointmentResponse;
import com.shopwise.model.Appointment;
import com.shopwise.model.enums.AppointmentStatus;
import com.shopwise.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
@Validated
public class AppointmentController {

    private AppointmentService appointmentService;

    @PostMapping("/")
    @ResponseStatus(code= HttpStatus.CREATED)
    public Appointment createAppointment(@Valid @RequestBody Appointment appointment){
        return appointmentService.createAppointment(appointment);
    }

    @GetMapping("/{merchantId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<AppointmentResponse> getAppointments(
            @PathVariable UUID merchantId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) String email) {
        return appointmentService.getAppointments(merchantId,date, status, email)
                .stream()
                .map(AppointmentResponse::from)
                .toList();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Appointment updateAppointmentStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, AppointmentStatus> body
    ) {
        return appointmentService.editAppointment(id,  body.get("status"));
    }

}
