package com.example.attendancebackend.controller;

import com.example.attendancebackend.service.AccountImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/accounts")
public class AccountImportController {

    private final AccountImportService accountImportService;

    public AccountImportController(
            AccountImportService accountImportService
    ) {
        this.accountImportService = accountImportService;
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> importAccounts(
            @RequestParam("file") MultipartFile file
    ) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Geen bestand geselecteerd");
        }

        try {
            accountImportService.importAccounts(
                    file.getInputStream()
            );

            return ResponseEntity.ok(
                    "Accounts succesvol geïmporteerd"
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Import mislukt: " + e.getMessage());
        }
    }
}