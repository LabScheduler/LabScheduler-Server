package com.example.labschedulerserver.payload.request;

import lombok.Data;

@Data
public class AddSubjectRequest {
    private String code;
    private String name;
    private int totalCredits;
    private int totalTheoryPeriods;
    private int totalPracticePeriods;
}
