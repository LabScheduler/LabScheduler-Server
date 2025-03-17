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
    private Integer subjectId;
    private Integer classId;
    private Integer lecturerId;
    private Integer totalStudents;
}
