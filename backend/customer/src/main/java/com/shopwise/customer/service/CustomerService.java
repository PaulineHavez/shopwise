package com.shopwise.customer.service;

import com.shopwise.customer.model.Customer;

import java.util.UUID;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    void deleteCustomer(UUID customerId);
}
