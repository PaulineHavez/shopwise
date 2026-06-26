package com.shopwise.service.impl;

import com.shopwise.dto.CustomerLoginResponse;
import com.shopwise.dto.CustomerRequest;
import com.shopwise.dto.LoginRequest;
import com.shopwise.dto.RegisterRequest;
import com.shopwise.exception.BadCredentialsException;
import com.shopwise.exception.CustomerAccountAlreadyExistsException;
import com.shopwise.exception.CustomerAlreadyExistsException;
import com.shopwise.exception.CustomerNotFoundException;
import com.shopwise.exception.MerchantNotFoundException;
import com.shopwise.model.Customer;
import com.shopwise.model.Merchant;
import com.shopwise.repository.AppointmentRepository;
import com.shopwise.repository.CustomerRepository;
import com.shopwise.repository.MerchantRepository;
import com.shopwise.repository.TransactionRepository;
import com.shopwise.service.CustomerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final MerchantRepository merchantRepository;
    private final TransactionRepository transactionRepository;
    private final AppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               MerchantRepository merchantRepository,
                               PasswordEncoder passwordEncoder,
                               TransactionRepository transactionRepository,
                               AppointmentRepository appointmentRepository) {
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.appointmentRepository = appointmentRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Customer createCustomer(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new CustomerAlreadyExistsException("email");
        }
        if (customerRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new CustomerAlreadyExistsException("phone number");
        }
        Merchant merchant = merchantRepository.findById(request.merchantId())
                .orElseThrow(MerchantNotFoundException::new);

        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setPhoneNumber(request.phoneNumber());
        customer.setEmail(request.email());
        customer.setMerchant(merchant);

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
    public Customer updateCustomer(UUID id, CustomerRequest request) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);

        if (!existing.getEmail().equals(request.email()) &&
                customerRepository.existsByEmail(request.email())) {
            throw new CustomerAlreadyExistsException("email");
        }

        if (!existing.getPhoneNumber().equals(request.phoneNumber()) &&
                customerRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new CustomerAlreadyExistsException("phone number");
        }

        existing.setEmail(request.email());
        existing.setPhoneNumber(request.phoneNumber());
        existing.setName(request.name());

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

        if (customer.getPassword() != null) {
            throw new CustomerAccountAlreadyExistsException();
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        customer.setPassword(encodedPassword);

        customerRepository.save(customer);

        return true;
    }

    @Override
    public Customer getCustomer(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);
    }

    @Override
    public Short getCustomerEarnedPoints(UUID customerId) {
        Integer transactionsEarnedPoints = transactionRepository.getEarnedPointsByCustomerId(customerId);
        Integer appointmentsEarnedPoints = appointmentRepository.getEarnedPointsByCustomerId(customerId);

        int total = (transactionsEarnedPoints != null ? transactionsEarnedPoints : 0)
                  + (appointmentsEarnedPoints != null ? appointmentsEarnedPoints : 0);

        return (short) total;
    }
}
