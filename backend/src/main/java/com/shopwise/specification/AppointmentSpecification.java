package com.shopwise.specification;

import com.shopwise.model.Appointment;
import com.shopwise.model.Customer;
import com.shopwise.model.enums.AppointmentStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AppointmentSpecification {

    public static Specification<Appointment> filterBy(UUID merchantId, LocalDate date, AppointmentStatus status, String email) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (merchantId != null) {
                predicates.add(cb.equal(root.get("merchantId"), merchantId));
            }

            if (date != null) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime startOfNextDay = date.plusDays(1).atStartOfDay();

                Predicate startsOnDate = cb.and(
                        cb.greaterThanOrEqualTo(root.get("startAt"), startOfDay),
                        cb.lessThan(root.get("startAt"), startOfNextDay)
                );
                Predicate endsOnDate = cb.and(
                        cb.greaterThanOrEqualTo(root.get("endAt"), startOfDay),
                        cb.lessThan(root.get("endAt"), startOfNextDay)
                );
                predicates.add(cb.or(startsOnDate, endsOnDate));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (email != null && !email.isBlank()) {
                Join<Appointment, Customer> customerJoin = root.join("customer");
                predicates.add(cb.equal(cb.lower(customerJoin.get("email")), email.toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}