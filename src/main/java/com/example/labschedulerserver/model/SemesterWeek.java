package com.example.labschedulerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id")
    @JsonIgnore
    private Semester semester;

    @OneToMany(mappedBy = "semesterWeek", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "newSemesterWeek", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ScheduleRequest> scheduleRequests;
}
