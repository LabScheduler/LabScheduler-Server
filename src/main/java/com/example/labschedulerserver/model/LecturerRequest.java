package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.RequestStatus;
import com.example.labschedulerserver.common.RequestType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
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
    @JsonProperty("lecturer_account")
    private LecturerAccount lecturerAccount;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "new_room_id")
    @JsonProperty("new_room")
    private Room newRoom;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    private CourseSection courseSection;

    @ManyToOne
    @JoinColumn(name = "new_semester_week_id")
    @JsonProperty("new_semester_week")
    private SemesterWeek newSemesterWeek;

    @Column(name = "new_day_of_week")
    @JsonProperty("new_day_of_week")
    private Byte newDayOfWeek;

    @Column(name = "new_start_period")
    @JsonProperty("new_start_period")
    private Byte newStartPeriod;

    @Column(name = "new_total_period")
    @JsonProperty("new_total_period")
    private Byte newTotalPeriod;

    private String reason;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @JsonProperty("type")
    private RequestType type;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    @UpdateTimestamp
    private Timestamp createdAt;

    @OneToOne(mappedBy = "request")
    @JsonProperty("lecturer_request_log")
    private LecturerRequestLog lecturerRequestLog;
}
