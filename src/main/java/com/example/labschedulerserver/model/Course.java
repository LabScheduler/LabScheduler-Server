package com.example.labschedulerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "class_id")
    @JsonProperty("class")
    private Clazz clazz;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    @JsonProperty("lecturer_account")
    private LecturerAccount lecturerAccount;

    @Column(name = "total_students")
    @JsonProperty("total_students")
    private Integer totalStudents;

    @OneToMany(mappedBy = "course")
    private List<CourseSection> courseSections;

    @OneToMany(mappedBy = "course",fetch = FetchType.LAZY)
    @JsonProperty("lecturer_requests")
    @JsonIgnore
    private List<LecturerRequest> lecturerRequests;

}
