package com.example.attendancebackend.controller;

import com.example.attendancebackend.model.AttendanceRequest;
import com.example.attendancebackend.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(
            AttendanceService attendanceService
    ) {
        this.attendanceService = attendanceService;
    }

    @PutMapping("/{eventId}/attendance")
    public ResponseEntity<String> setAttendance(
            @PathVariable Long eventId,
            @RequestBody AttendanceRequest request,
            Authentication authentication
    ) {

        attendanceService.setAttendance(
                eventId,
                authentication.getName(),
                request
        );

        return ResponseEntity.ok(
                "Attendance succesvol bijgewerkt"
        );
    }
}