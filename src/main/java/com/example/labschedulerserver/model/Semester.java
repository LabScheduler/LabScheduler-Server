package com.example.labschedulerserver.model;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Semester {
    private UUID id;
    private String name;
    private Date startDate;
    private Date endDate;
}
