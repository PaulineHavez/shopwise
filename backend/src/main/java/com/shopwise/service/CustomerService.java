package com.shopwise.service;

import com.shopwise.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    void deleteCustomer(UUID customerId);

    Customer updateCustomer(UUID id, Customer updatedCustomer);

    List<Customer> getCustomers();

    Customer getCustomerByEmail(String email);
}
