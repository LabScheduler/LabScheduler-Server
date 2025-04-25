package com.example.labschedulerserver.payload.response.Class;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassResponse {
    private Long id;
    private String name;
    private String major;
    private String specialization;
    private String type;
    @JsonProperty("number_of_students")
    private int numberOfStudents;
}
