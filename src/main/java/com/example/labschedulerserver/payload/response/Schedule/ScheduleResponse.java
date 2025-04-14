package com.example.labschedulerserver.payload.response.Schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
public class ScheduleResponse {
    private Long id;

    @JsonProperty("subject_code")
    private String subjectCode;

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("course_group")
    private Integer courseGroup;

    @JsonProperty("course_section")
    private Integer courseSection;

    private String room;

    @JsonProperty("day_of_week")
    private Byte dayOfWeek;

    @JsonProperty("start_period")
    private Byte startPeriod;

    @JsonProperty("total_period")
    private Byte totalPeriod;

    private String lecturer;

    private String type;

    @JsonProperty("semester_week")
    private String semesterWeek;

    @JsonProperty("study_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Asia/Ho_Chi_Minh")
    private Timestamp studyDate;

    private String status;
}
