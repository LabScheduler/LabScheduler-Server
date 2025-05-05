package com.example.labschedulerserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "lecturer_request")
public class LecturerRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private LecturerAccount lecturerAccount;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "course_section_id")
    private CourseSection courseSection;

    @ManyToOne
    @JoinColumn(name = "semester_week_id")
    private SemesterWeek semesterWeek;

    @Column(name = "day_of_week")
    private Byte dayOfWeek;

    @Column(name = "start_period")
    @JsonProperty("start_period")
    private Byte startPeriod;

    @Column(name = "total_period")
    private Byte totalPeriod;

    private String body;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    @UpdateTimestamp
    private Timestamp createdAt;

    @OneToOne(mappedBy = "request")
    @JsonProperty("lecturer_request_log")
    private LecturerRequestLog lecturerRequestLog;
}
