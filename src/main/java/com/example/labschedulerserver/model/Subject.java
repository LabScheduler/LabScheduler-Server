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
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    private String name;

    @Column(name = "total_credits")
    private Integer totalCredits;

    @Column(name = "total_class_periods")
    private Integer totalClassPeriods;

    @Column(name = "total_practice_periods")
    private Integer totalPracticePeriods;

    @OneToMany(mappedBy = "subject")
    private List<Course> courses;
}
