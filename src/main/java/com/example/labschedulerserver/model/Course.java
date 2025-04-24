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

    @Column(name = "group_number")
    @JsonProperty("group_number")
    private Integer groupNumber;

    @ManyToMany
    @JoinTable(
            name = "lecturer_on_course",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "lecturer_id")
    )
    private List<LecturerAccount> lecturers;

    @Column(name = "total_students")
    @JsonProperty("total_students")
    private Integer totalStudents;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<CourseSection> courseSections;

    @OneToMany(mappedBy = "course",fetch = FetchType.LAZY)
    @JsonProperty("lecturer_requests")
    @JsonIgnore
    private List<LecturerRequest> lecturerRequests;

    @OneToMany(mappedBy = "course",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Schedule> schedules;

}
