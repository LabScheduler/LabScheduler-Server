package com.example.labschedulerserver.payload.response;

import com.example.labschedulerserver.payload.response.Schedule.ScheduleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCourseResponse {
    private CourseResponse course;
    private List<ScheduleResponse> schedules;
}
