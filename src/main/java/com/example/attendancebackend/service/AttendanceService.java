package com.example.attendancebackend.service;

import com.example.attendancebackend.model.Account;
import com.example.attendancebackend.model.Attendance;
import com.example.attendancebackend.model.AttendanceRequest;
import com.example.attendancebackend.model.Event;
import com.example.attendancebackend.repository.AccountRepository;
import com.example.attendancebackend.repository.AttendanceRepository;
import com.example.attendancebackend.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AccountRepository accountRepository;
    private final EventRepository eventRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            AccountRepository accountRepository,
            EventRepository eventRepository
    ) {
        this.attendanceRepository = attendanceRepository;
        this.accountRepository = accountRepository;
        this.eventRepository = eventRepository;
    }

    public void setAttendance(
            Long eventId,
            String email,
            AttendanceRequest request
    ) {

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Account not found")
                );

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new RuntimeException("Event not found")
                );

        Attendance attendance =
                attendanceRepository
                        .findByAccountIdAndEventId(
                                account.getId(),
                                event.getId()
                        )
                        .orElse(null);

        if (attendance == null) {

            attendance = new Attendance();

            attendance.setAccount(account);
            attendance.setEvent(event);
        }

        attendance.setStatus(request.getStatus());
        attendance.setDateChanged(LocalDateTime.now());

        attendanceRepository.save(attendance);
    }
}