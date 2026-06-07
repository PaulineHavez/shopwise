package com.shopwise.repository;

import com.shopwise.model.Appointment;
import com.shopwise.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public interface AppointmentRepository  extends JpaRepository<Appointment, UUID> {

    @Query("""
    SELECT COUNT(a) > 0 FROM Appointment a
    WHERE a.serviceId = :serviceId
    AND a.startAt < :endAt
    AND a.endAt > :startAt
    AND a.status NOT IN :excludedStatuses
    """)
    boolean existsOverlappingAppointment(
            @Param("serviceId") UUID serviceId,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            @Param("excludedStatuses") Collection<AppointmentStatus> excludedStatuses
    );
}
