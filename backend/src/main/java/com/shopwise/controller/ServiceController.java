package com.shopwise.controller;

import com.shopwise.model.Service;
import com.shopwise.service.ServiceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
@AllArgsConstructor
@Validated
public class ServiceController {

    private ServiceService serviceService;

    @GetMapping("/{name}")
    @ResponseStatus(code= HttpStatus.OK)
    public Service getServiceByName(@PathVariable String name){
        return serviceService.getServiceByName(name);
    }
}