package com.shopwise.service.impl;

import com.shopwise.exception.ServiceNotFoundException;
import com.shopwise.model.Service;
import com.shopwise.repository.ServiceRepository;
import com.shopwise.service.ServiceService;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Service getServiceByName(String name) {
       return serviceRepository.findByName(name)
              .orElseThrow(ServiceNotFoundException::new);
    }
}
