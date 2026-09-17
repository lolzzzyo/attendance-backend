package com.example.attendancebackend.dto;

import com.example.attendancebackend.model.AttendanceStatus;

import java.time.LocalDateTime;

public class EventOverviewDto {

    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private String description;
    private AttendanceStatus myAttendance;

    public EventOverviewDto(Long id, LocalDateTime startDateTime, LocalDateTime endDateTime, String location, String description, AttendanceStatus attendanceStatus) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.location = location;
        this.description = description;
        this.myAttendance = attendanceStatus;
    }

    public Long getId() {
        return id;
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

    public AttendanceStatus getMyAttendance() {
        return myAttendance;
    }

    // constructor
    // getters
}