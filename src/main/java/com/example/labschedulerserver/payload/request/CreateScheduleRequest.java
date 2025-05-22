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

    private Long courseId;

    private Long courseSectionId;

    private Long roomId;

    private Long lecturerId;

    private Long semesterWeekId;

    private Byte dayOfWeek;

    private Byte startPeriod;

    private Byte totalPeriod;
}
