package com.shopwise.model;

import com.shopwise.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

import javax.net.ssl.SSLSession;
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
    private  AppointmentStatus status;

    @Column(name = "earned_points")
    private Short earnedPoints;

    @Column(name = "service_id", nullable = false)
    @NotNull
    private UUID serviceId;;

    @Column(name = "merchant_id", nullable = false)
    @NotNull
    private UUID merchantId;

    @Column(name = "customer_id", nullable = false)
    @NotNull
    private UUID customerId;
}
