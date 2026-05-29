package com.shopwise.service.impl;

import com.shopwise.exception.CustomerAlreadyExistsException;
import com.shopwise.exception.CustomerNotFoundException;
import com.shopwise.model.Customer;
import com.shopwise.repository.CustomerRepository;
import com.shopwise.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public Customer updateCustomer(UUID id, Customer updatedCustomer) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);

        if (!existing.getEmail().equals(updatedCustomer.getEmail()) &&
                customerRepository.existsByEmail(updatedCustomer.getEmail())) {
            throw new CustomerAlreadyExistsException("email");
        }

        if (!existing.getPhoneNumber().equals(updatedCustomer.getPhoneNumber()) &&
                customerRepository.existsByPhoneNumber(updatedCustomer.getPhoneNumber())) {
            throw new CustomerAlreadyExistsException("phone number");
        }

        existing.setEmail(updatedCustomer.getEmail());
        existing.setPhoneNumber(updatedCustomer.getPhoneNumber());
        existing.setName(updatedCustomer.getName());

        return customerRepository.save(existing);
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }
}
