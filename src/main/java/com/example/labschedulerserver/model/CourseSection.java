package com.example.labschedulerserver.model;

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
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "section_number")
    private Integer sectionNumber;

    @Column(name = "total_students_in_section")
    private Integer totalStudentsInSection;

    @OneToMany(mappedBy = "courseSection")
    private List<Schedule> schedules;
}
