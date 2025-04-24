package com.example.labschedulerserver.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class StudentOnClassId {
    private Long studentId;
    private Long classId;
}
