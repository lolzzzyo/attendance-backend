package com.example.attendancebackend.service;

import com.example.attendancebackend.dto.AccountResponseDto;
import com.example.attendancebackend.model.Account;
import com.example.attendancebackend.model.AccountUpdateRequest;
import com.example.attendancebackend.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void updateAccount(String email, AccountUpdateRequest request) {

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setFirstName(request.getFirstName());
        account.setNickname(request.getNickname());
        account.setLastName(request.getLastName());

        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            account.setPasswordHash(
                    passwordEncoder.encode(request.getPassword())
            );
        }

        accountRepository.save(account);
    }

    public AccountResponseDto getCurrentAccount(String email) {

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return new AccountResponseDto(
                account.getId(),
                account.getFirstName(),
                account.getNickname(),
                account.getLastName(),
                account.getEmail()
        );
    }
}