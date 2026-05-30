package com.shopwise.controller;

import com.shopwise.dto.LoginRequest;
import com.shopwise.model.Merchant;
import com.shopwise.service.CustomerService;
import com.shopwise.service.MerchantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/merchants")
@AllArgsConstructor
@Validated
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/{id}")
    @ResponseStatus(code= HttpStatus.OK)
    public Merchant getMerchantById(@PathVariable UUID id) {
        return merchantService.getMerchantById(id);
    }

    @PostMapping("/login")
    public Merchant login(@RequestBody LoginRequest request) {
        return merchantService.login(request);
    }
}