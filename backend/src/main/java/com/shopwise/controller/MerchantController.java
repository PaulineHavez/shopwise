package com.shopwise.controller;

import com.shopwise.dto.LoginRequest;
import com.shopwise.model.Merchant;
import com.shopwise.service.MerchantService;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<Merchant> getMerchantById(@PathVariable UUID id) {
        return ResponseEntity.ok(merchantService.getMerchantById(id));
    }

    @PostMapping("/login")
    public ResponseEntity<Merchant> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(merchantService.login(request));
    }
}