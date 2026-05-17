package com.shopwise.customer.controller;


import com.shopwise.customer.model.Customer;
import com.shopwise.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
@AllArgsConstructor
@Validated
public class CustomerController {

    private CustomerService customerService;

    @PostMapping("/")
    @ResponseStatus(code= HttpStatus.CREATED)
    public Customer createCustomer(@Valid @RequestBody Customer customer){
        return customerService.createCustomer(customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code= HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable UUID id){
        customerService.deleteCustomer(id);
    }

}
