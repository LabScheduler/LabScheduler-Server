package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "course_section_id")
    private CourseSection courseSection;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @Column(name = "start_period")
    private Integer startPeriod;

    @Column(name = "total_period")
    private Integer totalPeriod;

    @ManyToOne
    @JoinColumn(name = "semester_week_id")
    private SemesterWeek semesterWeek;

    @Column(name = "schedule_type")
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @Column(name = "schedule_status")
    @Enumerated(EnumType.STRING)
    private ScheduleStatus scheduleStatus;

    @OneToMany(mappedBy = "schedule")
    private List<ScheduleRequest> scheduleRequests;
}
