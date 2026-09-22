package com.example.attendancebackend.repository;

import com.example.attendancebackend.model.Attendance;
import com.example.attendancebackend.model.AttendanceStatus;
import com.example.attendancebackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByAccountIdAndEventId(
            Long accountId,
            Long eventId
    );

    List<Attendance> findByEventId(Long eventId);

    @Query("""
    SELECT COUNT(a)
    FROM Attendance a
    JOIN a.account acc
    WHERE a.event.id = :eventId
      AND a.status = :status
      AND acc.role = :role
""")
    long countByEventIdAndStatusAndRole(
            @Param("eventId") Long eventId,
            @Param("status") AttendanceStatus status,
            @Param("role") Role role
    );
}
