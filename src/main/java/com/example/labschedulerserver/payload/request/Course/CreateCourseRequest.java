package com.example.labschedulerserver.payload.request.Course;

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
    private Long subjectId;

    private Long classId;

    private List<Long> lecturersIds;

    private Long semesterId;

    private Integer totalStudents;

//    private Integer totalSection;

    private Long startWeekId;
}
