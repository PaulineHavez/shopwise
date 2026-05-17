package com.shopwise.customer.service.impl;

import com.shopwise.customer.exception.CustomerAlreadyExistsException;
import com.shopwise.customer.exception.CustomerNotFoundException;
import com.shopwise.customer.model.Customer;
import com.shopwise.customer.repository.CustomerRepository;
import com.shopwise.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl( CustomerRepository customerRepository) {this.customerRepository = customerRepository;}

    @Override
    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new CustomerAlreadyExistsException("email");
        }
        if (customerRepository.existsByPhoneNumber(customer.getPhoneNumber())) {
            throw new CustomerAlreadyExistsException("phone number");
        }
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException();
        }
        customerRepository.deleteById(id);
    }
}
