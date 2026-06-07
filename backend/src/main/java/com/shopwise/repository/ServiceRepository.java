package com.shopwise.repository;

import com.shopwise.model.Customer;
import com.shopwise.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<Service, UUID> {
    Optional<Service> findByName(String name);
}
