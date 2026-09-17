package com.example.attendancebackend.model;

import java.time.LocalDateTime;

public class EventUpdateRequest {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private String description;

    private EventScope updateScope;


    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventScope getUpdateScope() {
        return updateScope;
    }

    public void setUpdateScope(EventScope updateScope) {
        this.updateScope = updateScope;
    }
}