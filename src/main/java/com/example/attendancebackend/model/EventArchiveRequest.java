package com.example.attendancebackend.model;

public class EventArchiveRequest {

    private EventScope archiveScope;

    public EventScope getArchiveScope() {
        return archiveScope;
    }

    public void setArchiveScope(EventScope archiveScope) {
        this.archiveScope = archiveScope;
    }
}