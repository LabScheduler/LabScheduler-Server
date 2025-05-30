package com.example.labschedulerserver.payload.response.Report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private Long id;
    private String title;
    private String authorContent;
    private Timestamp createdAt;
    private String author;
    private String status;
    private String managerContent;
    private Timestamp updatedAt;
}
