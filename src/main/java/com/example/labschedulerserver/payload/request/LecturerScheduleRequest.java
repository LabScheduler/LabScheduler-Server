package com.example.labschedulerserver.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LecturerScheduleRequest {

    private Long courseId;

    private Long courseSectionId;

    private Long newRoomId;

    private Long newSemesterWeekId;

    private Byte newDayOfWeek;

    private Byte newStartPeriod;

    private Byte newTotalPeriod;

    private String body;

}
