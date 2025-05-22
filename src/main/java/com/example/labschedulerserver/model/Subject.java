package com.example.labschedulerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    @Column(name = "total_credits")
    private Integer totalCredits;

    @Column(name = "total_theory_periods")
    private Integer totalTheoryPeriods;

    @Column(name = "total_practice_periods")
    private Integer totalPracticePeriods;

    @Column(name = "total_exercise_periods")
    private Integer totalExercisePeriods;

    @Column(name = "total_self_study_periods")
    private Integer totalSelfStudyPeriods;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Course> courses;
}
