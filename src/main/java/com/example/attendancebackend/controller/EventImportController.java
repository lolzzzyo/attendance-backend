package com.example.attendancebackend.controller;

import com.example.attendancebackend.service.EventImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/events")
public class EventImportController {

    private final EventImportService eventImportService;

    public EventImportController(EventImportService eventImportService) {
        this.eventImportService = eventImportService;
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> importEvents(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Geen bestand geselecteerd");
        }

        try {
            eventImportService.importEvents(file.getInputStream());

            return ResponseEntity.ok(
                    "Events succesvol geïmporteerd"
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Import mislukt: " + e.getMessage());
        }
    }
}