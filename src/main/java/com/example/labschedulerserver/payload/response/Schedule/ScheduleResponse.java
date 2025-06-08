package com.example.labschedulerserver.payload.response.Schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleResponse {
    private Long id;

    private String subjectCode;

    private String subjectName;

    private Integer courseGroup;

    private Integer courseSection;

    private String room;

    private Byte dayOfWeek;

    private Byte startPeriod;

    private Byte totalPeriod;

    @JsonProperty("class")
    private String clazz;

    private String lecturer;

    private String semesterWeek;

    private String status;

    private String type;

}
