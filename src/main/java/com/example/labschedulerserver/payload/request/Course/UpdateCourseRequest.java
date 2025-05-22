package com.example.labschedulerserver.payload.request.Course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCourseRequest {

    private Long subjectId;

    private Long classId;
    private List<Long> lecturersIds;
    private Integer totalStudents;
}
