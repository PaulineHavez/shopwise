package com.shopwise.controller;

import com.shopwise.model.Appointment;
import com.shopwise.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}
