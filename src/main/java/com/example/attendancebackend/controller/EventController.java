package com.example.attendancebackend.controller;

import com.example.attendancebackend.dto.EventAttendanceOverviewDto;
import com.example.attendancebackend.dto.EventOverviewDto;
import com.example.attendancebackend.model.EventArchiveRequest;
import com.example.attendancebackend.model.EventCreateRequest;
import com.example.attendancebackend.model.EventUpdateRequest;
import com.example.attendancebackend.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping
    public List<EventOverviewDto> getUpcomingEvents(
            Authentication authentication) {

        return eventService.getUpcomingEventsForUser(
                authentication.getName());
    }

    @GetMapping("/past")
    public List<EventOverviewDto> getPastEvents(
            Authentication authentication) {

        return eventService.getPastEventsForUser(
                authentication.getName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<EventOverviewDto> getAllEvents(
            Authentication authentication
    ) {
        return eventService.getEventsForUser(
                authentication.getName()
        );
    }

    @GetMapping("/{eventId}/attendance")
    public EventAttendanceOverviewDto getEventAttendance(
            @PathVariable Long eventId) {

        return eventService.getEventAttendance(eventId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createEvent(
            @RequestBody EventCreateRequest request) {

        eventService.createEvent(request);

        return ResponseEntity.ok("Event succesvol aangemaakt");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{eventId}")
    public ResponseEntity<String> updateEvent(
            @PathVariable Long eventId,
            @RequestBody EventUpdateRequest request) {

        eventService.updateEvent(eventId, request);

        return ResponseEntity.ok("Event succesvol bijgewerkt");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{eventId}/archive")
    public ResponseEntity<String> archiveEvent(
            @PathVariable Long eventId,
            @RequestBody EventArchiveRequest request) {

        eventService.archiveEvent(eventId, request);

        return ResponseEntity.ok("Event succesvol gearchiveerd");
    }
}