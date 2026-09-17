package com.example.attendancebackend.model;


import com.example.attendancebackend.dto.EventOverviewDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_attendance_account_event",
                        columnNames = {"account_id", "event_id"}
                )
        }
)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    @Column(name = "date_changed", nullable = false)
    private LocalDateTime dateChanged;

    public void setAccount(Account account) {
        this.account = account;
    }
    public void setEvent(Event event) {
        this.event = event;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public void setDateChanged(LocalDateTime dateChanged) {
        this.dateChanged = dateChanged;
    }

    public AttendanceStatus getStatus() {
        return status;
    }
    public Account getAccount() {
        return account;
    }
}
