package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.ScheduleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "course_section_id")
    private CourseSection courseSection;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private LecturerAccount lecturer;

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

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;
}
