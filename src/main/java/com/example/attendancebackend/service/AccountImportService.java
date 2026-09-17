package com.example.attendancebackend.service;

import com.example.attendancebackend.importdata.ExcelAccountImporter;
import com.example.attendancebackend.model.Account;
import com.example.attendancebackend.model.Role;
import com.example.attendancebackend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class AccountImportService {

    private final ExcelAccountImporter excelAccountImporter;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-user-password}")
    private String defaultUserPassword;

    public AccountImportService(
            ExcelAccountImporter excelAccountImporter,
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.excelAccountImporter = excelAccountImporter;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void importAccounts(InputStream inputStream)
            throws IOException {

        List<Account> accounts =
                excelAccountImporter.importAccounts(inputStream);

        for (Account account : accounts) {

            // Skip existing accounts
            if (accountRepository.existsByEmail(account.getEmail())) {
                continue;
            }

            account.setPasswordHash(
                    passwordEncoder.encode(defaultUserPassword)
            );

            account.setRole(Role.USER);
            account.setActive(true);

            accountRepository.save(account);
        }
    }
}