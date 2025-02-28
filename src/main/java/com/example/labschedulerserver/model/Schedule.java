package com.example.labschedulerserver.model;

import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Schedule {
    private UUID id;
    private Integer dayOfWeek;
    private Integer startPeriod;
    private Integer totalPeriod;
    private ScheduleType scheduleType;
    private ScheduleStatus scheduleStatus;
}
