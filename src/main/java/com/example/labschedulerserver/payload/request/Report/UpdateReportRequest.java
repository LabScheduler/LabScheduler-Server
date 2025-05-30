package com.example.labschedulerserver.payload.request.Report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateReportRequest {
    private String title;
    private String content;
}
