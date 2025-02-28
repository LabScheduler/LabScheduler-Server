package com.example.labschedulerserver.model;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SemesterWeek {
    private UUID id;
    private String name;
    private Timestamp startTime;
    private Timestamp endTime;
}
