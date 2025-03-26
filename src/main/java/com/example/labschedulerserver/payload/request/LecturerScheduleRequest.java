package com.example.labschedulerserver.payload.request;

public class LecturerScheduleRequest {
    private Long lecturerId;
    private Long scheduleId;
    private Long courseId;
    private Long roomId;
    private Long semesterWeekId;
    private Byte dayOfWeek;
    private Byte newStartPeriod;
    private Byte newTotalPeriod;
    private String reason;
    private String type;
}
