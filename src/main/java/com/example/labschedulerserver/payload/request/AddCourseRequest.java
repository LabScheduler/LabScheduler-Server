package com.example.labschedulerserver.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCourseRequest {
    @JsonProperty("subject_id")
    private Long subjectId;
    @JsonProperty("class_id")
    private Long classId;
    @JsonProperty("lecturer_id")
    private Long lecturerId;
    @JsonProperty("total_students")
    private Integer totalStudents;
}
