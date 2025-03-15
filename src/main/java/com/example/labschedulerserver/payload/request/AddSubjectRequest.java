package com.example.labschedulerserver.payload.request;

import lombok.Data;

@Data

public class AddSubjectRequest {
    private String code;
    private String name;
    private int total_credits;
    private int total_theory_periods;
    private int total_practice_periods;
}
