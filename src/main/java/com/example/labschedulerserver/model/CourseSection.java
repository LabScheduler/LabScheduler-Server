package com.example.labschedulerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "course_section")
public class CourseSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "section_number")
    @JsonProperty("section_number")
    private Integer sectionNumber;

    @Column(name = "total_students_in_section")
    @JsonProperty("total_students_in_section")
    private Integer totalStudentsInSection;

    @OneToMany(mappedBy = "courseSection",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Schedule> schedules;
}
