package com.example.attendancebackend.dto;

import com.example.attendancebackend.model.AttendanceStatus;

public class EventUserAttendanceDto {

    private Long accountId;
    private String firstName;
    private String nickName;
    private String lastName;
    private AttendanceStatus attendance;

    public EventUserAttendanceDto(
            Long accountId,
            String firstName,
            String nickName,
            String lastName,
            AttendanceStatus attendance) {

        this.accountId = accountId;
        this.firstName = firstName;
        this.nickName = nickName;
        this.lastName = lastName;
        this.attendance = attendance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getLastName() {
        return lastName;
    }

    public AttendanceStatus getAttendance() {
        return attendance;
    }
}