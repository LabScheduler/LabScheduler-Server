package com.example.labschedulerserver.payload.response.LecturerRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class LecturerRequestResponse {
    private Long id;

    private String lecturer;

    private String subject;

    private Integer groupNumber;

    private Integer sectionNumber;

    private String newRoom;

    private String newSemesterWeek;

    private Byte newDayOfWeek;

    private Byte newStartPeriod;

    private Byte newTotalPeriod;

    private String lecturerBody;

    private String managerBody;

    private String status;

    private Timestamp createdAt;

}