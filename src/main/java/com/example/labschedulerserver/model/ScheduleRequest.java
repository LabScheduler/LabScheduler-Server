package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.RequestStatus;
import com.example.labschedulerserver.common.RequestType;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "schedule_request")
public class ScheduleRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private LecturerAccount lecturerAccount;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "new_room_id")
    private Room newRoom;

    @ManyToOne
    @JoinColumn(name = "new_semester_week_id")
    private SemesterWeek newSemesterWeek;

    @Column(name = "new_day_of_week")
    private Integer newDayOfWeek;

    @Column(name = "new_start_period")
    private Integer newStartPeriod;

    @Column(name = "new_total_period")
    private Integer newTotalPeriod;

    private String reason;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToOne(mappedBy = "request")
    private ScheduleRequestLog scheduleRequestLog;

}
