package com.example.labschedulerserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "semester_week")
public class SemesterWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(name = "start_time")
    private Timestamp startDate;

    @Column(name = "end_time")
    private Timestamp endDate;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @OneToMany(mappedBy = "semesterWeek")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "newSemesterWeek")
    private List<ScheduleRequest> scheduleRequests;
}
