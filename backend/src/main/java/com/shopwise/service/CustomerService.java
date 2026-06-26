package com.shopwise.service;

import com.shopwise.dto.CustomerLoginResponse;
import com.shopwise.dto.CustomerRequest;
import com.shopwise.dto.LoginRequest;
import com.shopwise.dto.RegisterRequest;
import com.shopwise.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Customer createCustomer(CustomerRequest request);

    void deleteCustomer(UUID customerId);

    Customer updateCustomer(UUID id, CustomerRequest request);

    List<Customer> getCustomers();

    Customer getCustomerByEmail(String email);

    CustomerLoginResponse login(LoginRequest request);

    Boolean register(RegisterRequest request);

    Customer getCustomer(UUID id);

    Short getCustomerEarnedPoints(UUID customerId);
}
