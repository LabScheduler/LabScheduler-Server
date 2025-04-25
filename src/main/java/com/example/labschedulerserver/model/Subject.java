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
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    @Column(name = "total_credits")
    @JsonProperty("total_credits")
    private Integer totalCredits;

    @Column(name = "total_theory_periods")
    @JsonProperty("total_theory_periods")
    private Integer totalTheoryPeriods;

    @Column(name = "total_practice_periods")
    @JsonProperty("total_practice_periods")
    private Integer totalPracticePeriods;

    @Column(name = "total_exercise_periods")
    @JsonProperty("total_exercise_periods")
    private Integer totalExercisePeriods;

    @Column(name = "total_self_study_periods")
    @JsonProperty("total_self_study_periods")
    private Integer totalSelfStudyPeriods;

    @OneToMany(mappedBy ="subject",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Course> courses;
}
