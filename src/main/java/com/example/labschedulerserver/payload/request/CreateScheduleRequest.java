package com.example.labschedulerserver.payload.request;

import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateScheduleRequest {
    private Long courseSectionId;
    private Long roomId;
    private Long semesterWeekId;
    private Byte dayOfWeek;
    private Byte startPeriod;
    private Byte totalPeriod;
    private ScheduleType type;
}
