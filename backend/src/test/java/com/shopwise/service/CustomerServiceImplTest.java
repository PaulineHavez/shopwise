package com.shopwise.service;

import com.shopwise.dto.CustomerLoginResponse;
import com.shopwise.dto.CustomerRequest;
import com.shopwise.dto.LoginRequest;
import com.shopwise.dto.RegisterRequest;
import com.shopwise.exception.BadCredentialsException;
import com.shopwise.exception.CustomerAccountAlreadyExistsException;
import com.shopwise.exception.CustomerAlreadyExistsException;
import com.shopwise.exception.CustomerNotFoundException;
import com.shopwise.model.Customer;
import com.shopwise.model.Merchant;
import com.shopwise.repository.AppointmentRepository;
import com.shopwise.repository.CustomerRepository;
import com.shopwise.repository.MerchantRepository;
import com.shopwise.repository.TransactionRepository;
import com.shopwise.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl service;

    private final UUID customerId = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private final UUID merchantId = UUID.fromString("00000000-0000-0000-0000-000000000002");

    private Customer buildCustomer() {
        Merchant merchant = new Merchant(merchantId, "Shop", "0600000000", "shop@example.com",
                "1 rue de la Paix", "12345678901234", "hashed", null);
        return new Customer(customerId, "Alice", "0601020304", "alice@example.com", merchant, null);
    }

    private CustomerRequest buildRequest() {
        return new CustomerRequest("Alice", "0601020304", "alice@example.com", merchantId);
    }

    private Merchant buildMerchant() {
        return new Merchant(merchantId, "Shop", "0600000000", "shop@example.com",
                "1 rue de la Paix", "12345678901234", "hashed", null);
    }

    // createCustomer

    @Test
    void createCustomer_emailAlreadyExists_throwsCustomerAlreadyExistsException() {
        when(customerRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.createCustomer(buildRequest()))
                .isInstanceOf(CustomerAlreadyExistsException.class);

        verify(customerRepository, never()).save(any());
    }

    @Test
    void createCustomer_phoneAlreadyExists_throwsCustomerAlreadyExistsException() {
        when(customerRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(customerRepository.existsByPhoneNumber("0601020304")).thenReturn(true);

        assertThatThrownBy(() -> service.createCustomer(buildRequest()))
                .isInstanceOf(CustomerAlreadyExistsException.class);

        verify(customerRepository, never()).save(any());
    }

    @Test
    void createCustomer_noConflict_returnsSavedCustomer() {
        Customer customer = buildCustomer();
        when(customerRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(customerRepository.existsByPhoneNumber("0601020304")).thenReturn(false);
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(buildMerchant()));
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = service.createCustomer(buildRequest());

        assertThat(result).isEqualTo(customer);
    }

    // deleteCustomer

    @Test
    void deleteCustomer_notFound_throwsCustomerNotFoundException() {
        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteCustomer(customerId))
                .isInstanceOf(CustomerNotFoundException.class);

        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    void deleteCustomer_exists_deletesSuccessfully() {
        when(customerRepository.existsById(customerId)).thenReturn(true);

        service.deleteCustomer(customerId);

        verify(customerRepository).deleteById(customerId);
    }

    // updateCustomer

    @Test
    void updateCustomer_notFound_throwsCustomerNotFoundException() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCustomer(customerId, buildRequest()))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void updateCustomer_newEmailAlreadyTaken_throwsCustomerAlreadyExistsException() {
        Customer existing = new Customer(customerId, "Alice", "0601020304", "old@example.com", buildMerchant(), null);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existing));
        when(customerRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.updateCustomer(customerId, buildRequest()))
                .isInstanceOf(CustomerAlreadyExistsException.class);
    }

    @Test
    void updateCustomer_newPhoneAlreadyTaken_throwsCustomerAlreadyExistsException() {
        Customer existing = new Customer(customerId, "Alice", "0000000000", "alice@example.com", buildMerchant(), null);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existing));
        when(customerRepository.existsByPhoneNumber("0601020304")).thenReturn(true);

        assertThatThrownBy(() -> service.updateCustomer(customerId, buildRequest()))
                .isInstanceOf(CustomerAlreadyExistsException.class);
    }

    @Test
    void updateCustomer_sameEmailAndPhone_updatesSuccessfully() {
        Customer existing = buildCustomer();
        CustomerRequest request = new CustomerRequest("Alice Renamed", "0601020304", "alice@example.com", merchantId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(existing);

        Customer result = service.updateCustomer(customerId, request);

        assertThat(result.getName()).isEqualTo("Alice Renamed");
        verify(customerRepository, never()).existsByEmail(any());
        verify(customerRepository, never()).existsByPhoneNumber(any());
    }

    @Test
    void updateCustomer_newEmailNotTaken_updatesSuccessfully() {
        Customer existing = new Customer(customerId, "Alice", "0601020304", "old@example.com", buildMerchant(), null);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existing));
        when(customerRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(customerRepository.save(existing)).thenReturn(existing);

        Customer result = service.updateCustomer(customerId, buildRequest());

        assertThat(result.getEmail()).isEqualTo("alice@example.com");
    }

    // getCustomers

    @Test
    void getCustomers_returnsAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(buildCustomer()));

        List<Customer> result = service.getCustomers();

        assertThat(result).hasSize(1);
    }

    // getCustomerByEmail

    @Test
    void getCustomerByEmail_notFound_throwsCustomerNotFoundException() {
        when(customerRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCustomerByEmail("unknown@example.com"))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void getCustomerByEmail_found_returnsCustomer() {
        Customer customer = buildCustomer();
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));

        Customer result = service.getCustomerByEmail(customer.getEmail());

        assertThat(result.getEmail()).isEqualTo("alice@example.com");
    }

    // login

    @Test
    void login_emailNotFound_throwsCustomerNotFoundException() {
        when(customerRepository.findByEmail("alice@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(new LoginRequest("alice@example.com", "secret")))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void login_wrongPassword_throwsBadCredentialsException() {
        Customer customer = new Customer(customerId, "Alice", "0601020304", "alice@example.com", buildMerchant(), "hashed");
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> service.login(new LoginRequest("alice@example.com", "wrong")))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void login_validCredentials_returnsLoginResponse() {
        Customer customer = new Customer(customerId, "Alice", "0601020304", "alice@example.com", buildMerchant(), "hashed");
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);

        CustomerLoginResponse response = service.login(new LoginRequest("alice@example.com", "secret"));

        assertThat(response.email()).isEqualTo("alice@example.com");
        assertThat(response.customerId()).isEqualTo(customerId);
    }

    // register

    @Test
    void register_emailNotFound_throwsCustomerNotFoundException() {
        when(customerRepository.findByEmail("alice@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.register(new RegisterRequest("alice@example.com", "pass")))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void register_passwordAlreadySet_throwsCustomerAccountAlreadyExistsException() {
        Customer customer = new Customer(customerId, "Alice", "0601020304", "alice@example.com", buildMerchant(), "existing_hash");
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> service.register(new RegisterRequest("alice@example.com", "pass")))
                .isInstanceOf(CustomerAccountAlreadyExistsException.class);
    }

    @Test
    void register_noExistingPassword_encodesAndSaves() {
        Customer customer = buildCustomer();
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(passwordEncoder.encode("pass")).thenReturn("encoded_pass");
        when(customerRepository.save(any())).thenReturn(customer);

        Boolean result = service.register(new RegisterRequest("alice@example.com", "pass"));

        assertThat(result).isTrue();
        verify(passwordEncoder).encode("pass");
        verify(customerRepository).save(customer);
    }

    // getCustomer

    @Test
    void getCustomer_notFound_throwsCustomerNotFoundException() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCustomer(customerId))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void getCustomer_found_returnsCustomer() {
        Customer customer = buildCustomer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Customer result = service.getCustomer(customerId);

        assertThat(result).isEqualTo(customer);
    }

    // getCustomerEarnedPoints

    @Test
    void getCustomerEarnedPoints_bothNull_returnsZero() {
        when(transactionRepository.getEarnedPointsByCustomerId(customerId)).thenReturn(null);
        when(appointmentRepository.getEarnedPointsByCustomerId(customerId)).thenReturn(null);

        short result = service.getCustomerEarnedPoints(customerId);

        assertThat(result).isEqualTo((short) 0);
    }

    @Test
    void getCustomerEarnedPoints_bothNonNull_returnsSum() {
        when(transactionRepository.getEarnedPointsByCustomerId(customerId)).thenReturn(30);
        when(appointmentRepository.getEarnedPointsByCustomerId(customerId)).thenReturn(20);

        short result = service.getCustomerEarnedPoints(customerId);

        assertThat(result).isEqualTo((short) 50);
    }

    @Test
    void getCustomerEarnedPoints_onlyTransactions_returnsTransactionPoints() {
        when(transactionRepository.getEarnedPointsByCustomerId(customerId)).thenReturn(40);
        when(appointmentRepository.getEarnedPointsByCustomerId(customerId)).thenReturn(null);

        short result = service.getCustomerEarnedPoints(customerId);

        assertThat(result).isEqualTo((short) 40);
    }

    @Test
    void getCustomerEarnedPoints_onlyAppointments_returnsAppointmentPoints() {
        when(transactionRepository.getEarnedPointsByCustomerId(customerId)).thenReturn(null);
        when(appointmentRepository.getEarnedPointsByCustomerId(customerId)).thenReturn(15);

        short result = service.getCustomerEarnedPoints(customerId);

        assertThat(result).isEqualTo((short) 15);
    }
}
