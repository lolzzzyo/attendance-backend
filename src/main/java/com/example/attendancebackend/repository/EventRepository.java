package com.example.attendancebackend.repository;

import com.example.attendancebackend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByExternalId(String externalId);

    List<Event> findAllByOrderByStartDateTimeAsc();

    List<Event> findByStartDateTimeBetweenOrderByStartDateTime(
            LocalDateTime start,
            LocalDateTime end);

    List<Event> findByArchivedFalseOrderByStartDateTime();

    List<Event> findByRecurrenceGroupId(UUID recurrenceGroupId);

    List<Event> findByArchivedFalseAndStartDateTimeGreaterThanEqualOrderByStartDateTimeAsc(
            LocalDateTime now);

    List<Event> findByArchivedFalseAndStartDateTimeLessThanOrderByStartDateTimeDesc(
            LocalDateTime now);
}
