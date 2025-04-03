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
public class LecturerScheduleRequest {
    @JsonProperty("lecturer_id")
    private Long lecturerId;

    @JsonProperty("schedule_id")
    private Long scheduleId;

    @JsonProperty("course_id")
    private Long courseId;

    @JsonProperty("course_section_id")
    private Long courseSectionId;

    @JsonProperty("new_room_id")
    private Long newRoomId;

    @JsonProperty("new_semester_week_id")
    private Long newSemesterWeekId;

    @JsonProperty("new_day_of_week")
    private Byte newDayOfWeek;

    @JsonProperty("new_start_period")
    private Byte newStartPeriod;

    @JsonProperty("new_total_period")
    private Byte newTotalPeriod;

    private String reason;

    private String type;
}
