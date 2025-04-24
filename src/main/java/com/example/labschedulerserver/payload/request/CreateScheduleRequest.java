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
public class CreateScheduleRequest {
    @JsonProperty("course_id")
    private Long courseId;
    @JsonProperty("course_section_id")
    private Long courseSectionId;
    @JsonProperty("room_id")
    private Long roomId;
    @JsonProperty("semester_week_id")
    private Long semesterWeekId;
    @JsonProperty("day_of_week")
    private Byte dayOfWeek;
    @JsonProperty("start_period")
    private Byte startPeriod;
    @JsonProperty("total_period")
    private Byte totalPeriod;
}
