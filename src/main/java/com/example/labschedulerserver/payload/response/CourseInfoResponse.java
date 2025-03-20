package com.example.labschedulerserver.payload.response;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.CourseSection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseInfoResponse {
    private Course course;
    private List<CourseSection> courseSections;
}
