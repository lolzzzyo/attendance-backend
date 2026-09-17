package com.example.attendancebackend.dto;

public class AccountResponseDto {

    private Long id;
    private String firstName;
    private String nickname;
    private String lastName;
    private String email;

    public AccountResponseDto(
            Long id,
            String firstName,
            String nickname,
            String lastName,
            String email) {

        this.id = id;
        this.firstName = firstName;
        this.nickname = nickname;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}