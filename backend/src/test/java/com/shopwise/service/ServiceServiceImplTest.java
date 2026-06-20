package com.shopwise.service;

import com.shopwise.exception.ServiceNotFoundException;
import com.shopwise.model.Service;
import com.shopwise.repository.ServiceRepository;
import com.shopwise.service.impl.ServiceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceServiceImpl service;

    private final UUID serviceId  = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private final UUID merchantId = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Test
    void getServiceByName_notFound_throwsServiceNotFoundException() {
        when(serviceRepository.findByName("Haircut")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getServiceByName("Haircut"))
                .isInstanceOf(ServiceNotFoundException.class);
    }

    @Test
    void getServiceByName_found_returnsService() {
        Service svc = new Service(serviceId, "Haircut", merchantId);
        when(serviceRepository.findByName("Haircut")).thenReturn(Optional.of(svc));

        Service result = service.getServiceByName("Haircut");

        assertThat(result.getName()).isEqualTo("Haircut");
        assertThat(result.getServiceId()).isEqualTo(serviceId);
    }
}
