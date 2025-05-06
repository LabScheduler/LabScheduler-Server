package com.example.labschedulerserver.payload.request.Course;

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
public class CreateCourseRequest {
    @JsonProperty("subject_id")
    private Long subjectId;

    @JsonProperty("class_id")
    private Long classId;

    @JsonProperty("lecturer_id")
    private List<Long> lecturersId;

    @JsonProperty("semester_id")
    private Long semesterId;

    @JsonProperty("total_students")
    private Integer totalStudents;

    @JsonProperty("total_section")
    private Integer totalSection;
}
