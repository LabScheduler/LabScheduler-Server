package com.example.labschedulerserver.utils;

import com.example.labschedulerserver.model.Room;
import com.example.labschedulerserver.model.SemesterWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleSlot {
    private Room room;
    private SemesterWeek semesterWeek;
    private byte dayOfWeek;
    private byte startPeriod;
}
