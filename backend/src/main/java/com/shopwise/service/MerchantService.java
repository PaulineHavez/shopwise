package com.shopwise.service;

import com.shopwise.dto.LoginRequest;
import com.shopwise.dto.LoginResponse;
import com.shopwise.model.Merchant;

import java.util.UUID;

public interface MerchantService {

    Merchant getMerchantById(UUID id);

    LoginResponse login(LoginRequest request);
}
