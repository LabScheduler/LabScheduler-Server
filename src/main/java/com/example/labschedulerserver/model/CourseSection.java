package com.example.labschedulerserver.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseSection {
    private UUID id;
    private Integer sectionNumber;
    private Integer totalStudents;
}
