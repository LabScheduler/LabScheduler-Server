package com.example.labschedulerserver.payload.response;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.CourseSection;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponse {
    private Long id;
    private String subject;
    @JsonProperty("class")
    private String clazz;
    private String semester;
    private List<String> lecturers;
    private Integer groupNumber;
    private Integer maxStudents;
    private int totalStudents;

}
