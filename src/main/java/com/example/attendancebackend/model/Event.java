package com.example.attendancebackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalId;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDateTime;
    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDateTime;

    private String location;
    private String description;
    private boolean archived;
    private UUID recurrenceGroupId;

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
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



    public String getExternalId() {
        return externalId;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
    public boolean isArchived() {
        return archived;
    }

    public UUID getRecurrenceGroupId() {
        return recurrenceGroupId;
    }

    public void setRecurrenceGroupId(UUID recurrenceGroupId) {
        this.recurrenceGroupId = recurrenceGroupId;
    }
}