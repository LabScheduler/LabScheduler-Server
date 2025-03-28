package com.example.labschedulerserver.payload.request.Course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateCourseRequest {
    @JsonProperty("subject_id")
    private Long subjectId;
    @JsonProperty("class_id")
    private Long classId;
    @JsonProperty("lecturer_id")
    private Long lecturerId;
    @JsonProperty("total_students")
    private Integer totalStudents;
}
