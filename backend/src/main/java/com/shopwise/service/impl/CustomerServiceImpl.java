package com.shopwise.service.impl;

import com.shopwise.dto.CustomerLoginResponse;
import com.shopwise.dto.LoginRequest;
import com.shopwise.dto.RegisterRequest;
import com.shopwise.exception.BadCredentialsException;
import com.shopwise.exception.CustomerAccountAlreadyExistsException;
import com.shopwise.exception.CustomerAlreadyExistsException;
import com.shopwise.exception.CustomerNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.shopwise.model.Customer;
import com.shopwise.repository.CustomerRepository;
import com.shopwise.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;


    public CustomerServiceImpl( CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(CustomerNotFoundException::new);
    }

    public CustomerLoginResponse login(LoginRequest request) {
        Customer customer = customerRepository.findByEmail(request.email())
                .orElseThrow(CustomerNotFoundException::new);

        if (!passwordEncoder.matches(request.password(), customer.getPassword())) {
            throw new BadCredentialsException();
        }

        return new CustomerLoginResponse(
                customer.getCustomerId(),
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getEmail()
        );
    }

    @Override
    public Boolean register(RegisterRequest request) {
        Customer customer = customerRepository.findByEmail(request.email())
                .orElseThrow(CustomerNotFoundException::new);

        if(customer.getPassword() != null){
            throw new CustomerAccountAlreadyExistsException();
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        customer.setPassword(encodedPassword);

        customerRepository.save(customer);

        return true;
    }

    @Override
    public Customer getCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);
      return customer;
    }
}
