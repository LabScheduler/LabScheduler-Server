package com.example.labschedulerserver.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCourseRequest {
    private Long subjectId;
    private Long classId;
    private Long lecturerId;
    private Integer totalStudents;
}
