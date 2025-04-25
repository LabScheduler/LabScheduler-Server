package com.example.labschedulerserver.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddSubjectRequest {
    private String code;
    private String name;
    @JsonProperty("total_credits")
    private int totalCredits;
    @JsonProperty("total_theory_periods")
    private int totalTheoryPeriods;
    @JsonProperty("total_practice_periods")
    private Integer totalPracticePeriods;
    @JsonProperty("total_exercise_periods")
    private Integer totalExercisePeriods;
    @JsonProperty("total_self_study_periods")
    private Integer totalSelfStudyPeriods;
}
