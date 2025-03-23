package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_section_id")
    private CourseSection courseSection;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "day_of_week")
    private Byte dayOfWeek;

    @Column(name = "start_period")
    private Byte startPeriod;

    @Column(name = "total_period")
    private Byte totalPeriod;

    @ManyToOne
    @JoinColumn(name = "semester_week_id")
    private SemesterWeek semesterWeek;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ScheduleStatus scheduleStatus;

    @OneToMany(mappedBy = "schedule",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ScheduleRequest> scheduleRequests;
}
