package com.example.labschedulerserver.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddSubjectRequest {
    private String code;
    private String name;
    private int totalCredits;

    private int totalTheoryPeriods;

    private Integer totalPracticePeriods;

    private Integer totalExercisePeriods;

    private Integer totalSelfStudyPeriods;
}
