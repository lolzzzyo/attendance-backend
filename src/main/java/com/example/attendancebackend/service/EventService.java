package com.example.attendancebackend.service;

import com.example.attendancebackend.dto.EventAttendanceOverviewDto;
import com.example.attendancebackend.dto.EventOverviewDto;
import com.example.attendancebackend.dto.EventUserAttendanceDto;
import com.example.attendancebackend.model.*;
import com.example.attendancebackend.repository.AccountRepository;
import com.example.attendancebackend.repository.AttendanceRepository;
import com.example.attendancebackend.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final AccountRepository accountRepository;
    private final AttendanceRepository attendanceRepository;

    public EventService(
            EventRepository eventRepository,
            AccountRepository accountRepository,
            AttendanceRepository attendanceRepository
    ) {
        this.eventRepository = eventRepository;
        this.accountRepository = accountRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<EventOverviewDto> getUpcomingEventsForUser(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<Event> events =
                eventRepository
                        .findByArchivedFalseAndStartDateTimeGreaterThanEqualOrderByStartDateTimeAsc(
                                LocalDateTime.now());

        return events.stream()
                .map(event -> {
                    Attendance attendance =
                            attendanceRepository
                                    .findByAccountIdAndEventId(
                                            account.getId(),
                                            event.getId())
                                    .orElse(null);

                    long attendingCount =
                            attendanceRepository.countByEventIdAndStatusAndRole(
                                    event.getId(),
                                    AttendanceStatus.ATTENDING,
                                    Role.USER);

                    return new EventOverviewDto(
                            event.getId(),
                            event.getStartDateTime(),
                            event.getEndDateTime(),
                            event.getLocation(),
                            event.getDescription(),
                            attendance != null
                                    ? attendance.getStatus()
                                    : null,
                            attendingCount,
                            event.getRecurrenceGroupId()
                    );
                })
                .toList();
    }

    public List<EventOverviewDto> getPastEventsForUser(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<Event> events =
                eventRepository
                        .findByArchivedFalseAndStartDateTimeLessThanOrderByStartDateTimeDesc(
                                LocalDateTime.now());

        return events.stream()
                .map(event -> {
                    Attendance attendance =
                            attendanceRepository
                                    .findByAccountIdAndEventId(
                                            account.getId(),
                                            event.getId())
                                    .orElse(null);

                    long attendingCount =
                            attendanceRepository.countByEventIdAndStatusAndRole(
                                    event.getId(),
                                    AttendanceStatus.ATTENDING,
                                    Role.USER);

                    return new EventOverviewDto(
                            event.getId(),
                            event.getStartDateTime(),
                            event.getEndDateTime(),
                            event.getLocation(),
                            event.getDescription(),
                            attendance != null
                                    ? attendance.getStatus()
                                    : null,
                            attendingCount,
                            event.getRecurrenceGroupId()
                    );
                })
                .toList();
    }

    public void createEvent(EventCreateRequest request) {

        validateCreateRequest(request);

        RecurrenceType recurrence = request.getRecurrence();

        if (recurrence == RecurrenceType.NONE) {
            Event event = eventCreateInstance(
                    request.getStartDateTime(),
                    request.getEndDateTime(),
                    request.getLocation(),
                    request.getDescription(),
                    null
            );

            eventRepository.save(event);
            return;
        }

        UUID recurrenceGroupId = UUID.randomUUID();

        List<Event> events = new ArrayList<>();

        LocalDateTime currentStart = request.getStartDateTime();
        Duration duration = Duration.between(
                request.getStartDateTime(),
                request.getEndDateTime()
        );

        while (!currentStart.isAfter(request.getRepeatUntil())) {

            LocalDateTime currentEnd = currentStart.plus(duration);

            events.add(eventCreateInstance(
                    currentStart,
                    currentEnd,
                    request.getLocation(),
                    request.getDescription(),
                    recurrenceGroupId
            ));

            currentStart = getNextOccurrence(
                    currentStart,
                    recurrence
            );
        }

        eventRepository.saveAll(events);
    }

    public void updateEvent(Long eventId, EventUpdateRequest request) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        validateUpdateRequest(event, request);

        List<Event> eventsToUpdate =
                getEventsToUpdate(event, request.getUpdateScope());

        LocalTime newStartTime =
                request.getStartDateTime().toLocalTime();

        LocalTime newEndTime =
                request.getEndDateTime().toLocalTime();

        for (Event eventToUpdate : eventsToUpdate) {

            eventToUpdate.setLocation(request.getLocation());
            eventToUpdate.setDescription(request.getDescription());

            if (request.getUpdateScope() == EventScope.THIS_EVENT) {

                eventToUpdate.setStartDateTime(
                        request.getStartDateTime()
                );

                eventToUpdate.setEndDateTime(
                        request.getEndDateTime()
                );

            } else {

                LocalDate eventDate =
                        eventToUpdate.getStartDateTime().toLocalDate();

                eventToUpdate.setStartDateTime(
                        LocalDateTime.of(eventDate, newStartTime)
                );

                eventToUpdate.setEndDateTime(
                        LocalDateTime.of(eventDate, newEndTime)
                );
            }
        }

        eventRepository.saveAll(eventsToUpdate);
    }

    public void archiveEvent(
            Long eventId,
            EventArchiveRequest request) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        validateArchiveRequest(event, request);

        List<Event> eventsToArchive =
                getEventsToArchive(
                        event,
                        request.getArchiveScope()
                );

        for (Event eventToArchive : eventsToArchive) {
            eventToArchive.setArchived(true);
        }

        eventRepository.saveAll(eventsToArchive);
    }

    public List<EventOverviewDto> getEventsForUser(String email) {

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Account not found")
                );

        List<Event> events = eventRepository.findAllByOrderByStartDateTimeAsc();

        return events.stream()
                .map(event -> {

                    Attendance attendance =
                            attendanceRepository
                                    .findByAccountIdAndEventId(
                                            account.getId(),
                                            event.getId()
                                    )
                                    .orElse(null);

                    long attendingCount =
                            attendanceRepository.countByEventIdAndStatusAndRole(
                                    event.getId(),
                                    AttendanceStatus.ATTENDING,
                                    Role.USER);

                    return new EventOverviewDto(
                            event.getId(),
                            event.getStartDateTime(),
                            event.getEndDateTime(),
                            event.getLocation(),
                            event.getDescription(),
                            attendance != null
                                    ? attendance.getStatus()
                                    : null,
                            attendingCount,
                            event.getRecurrenceGroupId()
                    );
                })
                .toList();
    }


    public EventAttendanceOverviewDto getEventAttendance(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Attendance> attendances =
                attendanceRepository.findByEventId(eventId);

        Map<Long, AttendanceStatus> statusByAccountId =
                attendances.stream()
                        .collect(Collectors.toMap(
                                attendance -> attendance.getAccount().getId(),
                                Attendance::getStatus
                        ));

        List<EventUserAttendanceDto> users =
                accountRepository.findByRole(Role.USER)
                        .stream()
                        .map(account -> new EventUserAttendanceDto(
                                account.getId(),
                                account.getFirstName(),
                                account.getLastName(),
                                statusByAccountId.get(account.getId())
                        ))
                        .toList();

        return new EventAttendanceOverviewDto(
                event.getId(),
                event.getStartDateTime(),
                event.getEndDateTime(),
                event.getLocation(),
                event.getDescription(),
                users
        );
    }

    private Event eventCreateInstance(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String location,
            String description,
            UUID recurrenceGroupId) {

        Event event = new Event();

        event.setStartDateTime(startDateTime);
        event.setEndDateTime(endDateTime);
        event.setLocation(location);
        event.setDescription(description);
        event.setRecurrenceGroupId(recurrenceGroupId);

        return event;
    }

    private LocalDateTime getNextOccurrence(
            LocalDateTime current,
            RecurrenceType recurrence) {

        return switch (recurrence) {

            case DAILY ->
                    current.plusDays(1);

            case WEEKLY ->
                    current.plusWeeks(1);

            case MONTHLY ->
                    getNextMonthlyOccurrence(current);

            case YEARLY ->
                    getNextYearlyOccurrence(current);

            case NONE ->
                    throw new IllegalArgumentException(
                            "Recurrence NONE does not have multiple occurrences"
                    );
        };
    }

    private LocalDateTime getNextMonthlyOccurrence(
            LocalDateTime current) {

        LocalDate date = current.toLocalDate();

        YearMonth nextMonth = YearMonth.from(date).plusMonths(1);

        int day = Math.min(
                date.getDayOfMonth(),
                nextMonth.lengthOfMonth()
        );

        return LocalDateTime.of(
                nextMonth.getYear(),
                nextMonth.getMonthValue(),
                day,
                current.getHour(),
                current.getMinute(),
                current.getSecond(),
                current.getNano()
        );
    }

    private LocalDateTime getNextYearlyOccurrence(
            LocalDateTime current) {

        LocalDate date = current.toLocalDate();

        int year = date.getYear() + 1;

        int day = Math.min(
                date.getDayOfMonth(),
                YearMonth.of(year, date.getMonthValue()).lengthOfMonth()
        );

        return LocalDateTime.of(
                year,
                date.getMonthValue(),
                day,
                current.getHour(),
                current.getMinute(),
                current.getSecond(),
                current.getNano()
        );
    }

    private void validateCreateRequest(EventCreateRequest request) {

        if (request.getStartDateTime() == null) {
            throw new IllegalArgumentException(
                    "Start date/time is required"
            );
        }

        if (request.getEndDateTime() == null) {
            throw new IllegalArgumentException(
                    "End date/time is required"
            );
        }

        if (!request.getEndDateTime().isAfter(
                request.getStartDateTime())) {

            throw new IllegalArgumentException(
                    "End date/time must be after start date/time"
            );
        }

        if (request.getRecurrence() == null) {
            throw new IllegalArgumentException(
                    "Recurrence is required"
            );
        }

        if (request.getRecurrence() != RecurrenceType.NONE
                && request.getRepeatUntil() == null) {

            throw new IllegalArgumentException(
                    "repeatUntil is required for recurring events"
            );
        }

        if (request.getRecurrence() != RecurrenceType.NONE
                && request.getRepeatUntil().isBefore(
                request.getStartDateTime())) {

            throw new IllegalArgumentException(
                    "repeatUntil must be on or after the start date/time"
            );
        }
    }

    private List<Event> getEventsToUpdate(
            Event event,
            EventScope updateScope) {

        if (updateScope == EventScope.THIS_EVENT) {
            return List.of(event);
        }

        if (event.getRecurrenceGroupId() == null) {
            throw new IllegalArgumentException(
                    "This event is not part of a recurrence"
            );
        }

        List<Event> recurrenceEvents =
                eventRepository.findByRecurrenceGroupId(
                        event.getRecurrenceGroupId()
                );

        if (updateScope == EventScope.ENTIRE_RECURRENCE) {
            return recurrenceEvents;
        }

        return recurrenceEvents.stream()
                .filter(otherEvent ->
                        !otherEvent.getStartDateTime()
                                .isBefore(event.getStartDateTime()))
                .toList();
    }

    private void validateUpdateRequest(
            Event event,
            EventUpdateRequest request) {

        if (event.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Past events cannot be changed"
            );
        }

        if (request.getStartDateTime() == null) {
            throw new IllegalArgumentException(
                    "Start date/time is required"
            );
        }

        if (request.getEndDateTime() == null) {
            throw new IllegalArgumentException(
                    "End date/time is required"
            );
        }

        if (!request.getEndDateTime().isAfter(
                request.getStartDateTime())) {
            throw new IllegalArgumentException(
                    "End date/time must be after start date/time"
            );
        }

        if (request.getUpdateScope() == null) {
            throw new IllegalArgumentException(
                    "Update scope is required"
            );
        }

        if (event.getRecurrenceGroupId() == null
                && request.getUpdateScope() != EventScope.THIS_EVENT) {
            throw new IllegalArgumentException(
                    "A non-recurring event can only be updated individually"
            );
        }
    }

    private List<Event> getEventsToArchive(
            Event event,
            EventScope archiveScope) {

        if (archiveScope == EventScope.THIS_EVENT) {
            return List.of(event);
        }

        List<Event> recurrenceEvents =
                eventRepository.findByRecurrenceGroupId(
                        event.getRecurrenceGroupId()
                );

        if (archiveScope == EventScope.ENTIRE_RECURRENCE) {
            return recurrenceEvents;
        }

        return recurrenceEvents.stream()
                .filter(otherEvent ->
                        !otherEvent.getStartDateTime()
                                .isBefore(event.getStartDateTime()))
                .toList();
    }

    private void validateArchiveRequest(
            Event event,
            EventArchiveRequest request) {

        if (request.getArchiveScope() == null) {
            throw new IllegalArgumentException(
                    "Archive scope is required"
            );
        }

        if (event.getRecurrenceGroupId() == null
                && request.getArchiveScope() != EventScope.THIS_EVENT) {

            throw new IllegalArgumentException(
                    "A non-recurring event can only be archived individually"
            );
        }
    }
}
