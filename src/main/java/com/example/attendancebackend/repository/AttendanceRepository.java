package com.example.attendancebackend.repository;

import com.example.attendancebackend.model.Attendance;
import com.example.attendancebackend.model.AttendanceStatus;
import com.example.attendancebackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByAccountIdAndEventId(
            Long accountId,
            Long eventId
    );

    List<Attendance> findByEventId(Long eventId);

    long countByEventIdAndStatusAndRole(Long eventId, AttendanceStatus status, Role role);
}
