package com.shopwise.service.impl;
import com.shopwise.dto.LoginRequest;
import com.shopwise.dto.LoginResponse;
import com.shopwise.exception.BadCredentialsException;
import com.shopwise.exception.MerchantNotFoundException;
import com.shopwise.model.Merchant;
import com.shopwise.repository.MerchantRepository;
import com.shopwise.service.MerchantService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;

    public MerchantServiceImpl( MerchantRepository merchantRepository, PasswordEncoder passwordEncoder) {
        this.merchantRepository = merchantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Merchant getMerchantById(UUID id) {
       return merchantRepository.findById(id)
                .orElseThrow(MerchantNotFoundException::new);
    }

    public LoginResponse login(LoginRequest request) {
        Merchant merchant = merchantRepository.findByEmail(request.email())
                .orElseThrow(MerchantNotFoundException::new);

        if (!passwordEncoder.matches(request.password(), merchant.getPassword())) {
            throw new BadCredentialsException();
        }

        return new LoginResponse(
                merchant.getMerchantId(),
                merchant.getName(),
                merchant.getEmail()
        );
    }
}
