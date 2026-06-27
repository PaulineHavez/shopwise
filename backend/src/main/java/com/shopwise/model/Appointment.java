package com.shopwise.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopwise.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "APPOINTMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "appointment_id", updatable = false, nullable = false)
    private UUID appointmentId;

    @Column(name = "start_at", nullable = false)
    @NotNull
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    @NotNull
    private LocalDateTime endAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "earned_points")
    private Short earnedPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    @JsonIgnore
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    @JsonIgnore
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;
}
