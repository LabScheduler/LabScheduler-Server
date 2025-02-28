package com.example.labschedulerserver.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {
    private UUID id;
    private String code;
    private String name;
    private Integer totalCredits;
    private Integer totalClassPeriods;
    private Integer totalPracticePeriods;
}
