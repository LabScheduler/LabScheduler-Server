package com.example.labschedulerserver.payload.response;

import com.example.labschedulerserver.model.SemesterWeek;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SemesterResponse {
    private Long id;
    private String name;
    @JsonProperty("start_date")
    private Timestamp startDate;
    @JsonProperty("end_date")
    private Timestamp endDate;
    @JsonProperty("semester_weeks")
    private List<SemesterWeek> semesterWeeks;

}
