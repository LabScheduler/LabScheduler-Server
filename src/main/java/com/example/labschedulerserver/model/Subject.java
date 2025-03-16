package com.example.labschedulerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "total_theory_periods")
    private Integer totalTheoryPeriods;

    @Column(name = "total_practice_periods")
    private Integer totalPracticePeriods;

    @OneToMany(mappedBy = "subject",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Course> courses;
}
