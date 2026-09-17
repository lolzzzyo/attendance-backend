package com.example.attendancebackend.controller;

import com.example.attendancebackend.dto.AccountResponseDto;
import com.example.attendancebackend.model.AccountUpdateRequest;
import com.example.attendancebackend.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateAccount(
            @RequestBody AccountUpdateRequest request,
            Authentication authentication) {

        accountService.updateAccount(
                authentication.getName(),
                request
        );

        return ResponseEntity.ok("Account succesvol bijgewerkt");
    }

    @GetMapping("/me")
    public AccountResponseDto getCurrentAccount(
            Authentication authentication) {

        return accountService.getCurrentAccount(
                authentication.getName()
        );
    }
}
