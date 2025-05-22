package com.example.labschedulerserver.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSemesterRequest {

    private String code;
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private int startWeek;


}
