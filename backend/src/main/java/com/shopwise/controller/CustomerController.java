package com.shopwise.controller;


import com.shopwise.model.Customer;
import com.shopwise.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
@Validated
public class CustomerController {

    private CustomerService customerService;

    @GetMapping("/")
    @ResponseStatus(code= HttpStatus.OK)
    public List<Customer> getCustomers(){
        return customerService.getCustomers();
    }

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

    @PutMapping("/{id}")
    @ResponseStatus(code= HttpStatus.OK)
    public Customer updateCustomer(@PathVariable UUID id, @Valid @RequestBody Customer updateCustomer){
        return customerService.updateCustomer(id,updateCustomer);
    }

    // URL : /api/customers/test@test.com
    @GetMapping("/email/{email}")
    public Customer getByEmail(@PathVariable String email) {
        return customerService.getCustomerByEmail(email);
    }
}
