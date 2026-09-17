package com.example.attendancebackend.repository;

import com.example.attendancebackend.model.Attendance;
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
}
