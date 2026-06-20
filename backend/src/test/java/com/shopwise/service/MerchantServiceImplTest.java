package com.shopwise.service;

import com.shopwise.dto.LoginRequest;
import com.shopwise.dto.MerchantLoginResponse;
import com.shopwise.exception.BadCredentialsException;
import com.shopwise.exception.MerchantNotFoundException;
import com.shopwise.model.Merchant;
import com.shopwise.repository.MerchantRepository;
import com.shopwise.service.impl.MerchantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MerchantServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MerchantServiceImpl service;

    private final UUID merchantId = UUID.fromString("00000000-0000-0000-0000-000000000002");

    private Merchant buildMerchant() {
        return new Merchant(merchantId, "Shop", "0601020304", "shop@example.com",
                "1 rue de la Paix", "12345678901234", "hashed", (short) 10);
    }

    // getMerchantById

    @Test
    void getMerchantById_notFound_throwsMerchantNotFoundException() {
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getMerchantById(merchantId))
                .isInstanceOf(MerchantNotFoundException.class);
    }

    @Test
    void getMerchantById_found_returnsMerchant() {
        Merchant merchant = buildMerchant();
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(merchant));

        Merchant result = service.getMerchantById(merchantId);

        assertThat(result.getMerchantId()).isEqualTo(merchantId);
        assertThat(result.getEmail()).isEqualTo("shop@example.com");
    }

    // login

    @Test
    void login_emailNotFound_throwsMerchantNotFoundException() {
        when(merchantRepository.findByEmail("shop@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(new LoginRequest("shop@example.com", "secret")))
                .isInstanceOf(MerchantNotFoundException.class);
    }

    @Test
    void login_wrongPassword_throwsBadCredentialsException() {
        Merchant merchant = buildMerchant();
        when(merchantRepository.findByEmail(merchant.getEmail())).thenReturn(Optional.of(merchant));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> service.login(new LoginRequest("shop@example.com", "wrong")))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void login_validCredentials_returnsMerchantLoginResponse() {
        Merchant merchant = buildMerchant();
        when(merchantRepository.findByEmail(merchant.getEmail())).thenReturn(Optional.of(merchant));
        when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);

        MerchantLoginResponse response = service.login(new LoginRequest("shop@example.com", "secret"));

        assertThat(response.merchantId()).isEqualTo(merchantId);
        assertThat(response.email()).isEqualTo("shop@example.com");
        assertThat(response.name()).isEqualTo("Shop");
    }
}
