package com.example.attendancebackend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EventAttendanceOverviewDto {

    private Long eventId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private String description;
    private List<EventUserAttendanceDto> users;

    public EventAttendanceOverviewDto(
            Long eventId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String location,
            String description,
            List<EventUserAttendanceDto> users) {

        this.eventId = eventId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.location = location;
        this.description = description;
        this.users = users;
    }

    public Long getEventId() {
        return eventId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public List<EventUserAttendanceDto> getUsers() {
        return users;
    }
}