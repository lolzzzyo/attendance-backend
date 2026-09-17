package com.example.attendancebackend.dto;

import com.example.attendancebackend.model.AttendanceStatus;

public class EventUserAttendanceDto {

    private Long accountId;
    private String firstName;
    private String lastName;
    private AttendanceStatus attendance;

    public EventUserAttendanceDto(
            Long accountId,
            String firstName,
            String lastName,
            AttendanceStatus attendance) {

        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.attendance = attendance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public AttendanceStatus getAttendance() {
        return attendance;
    }
}