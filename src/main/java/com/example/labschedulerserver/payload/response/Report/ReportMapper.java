package com.example.labschedulerserver.payload.response.Report;


import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.model.Report;
import com.example.labschedulerserver.model.ReportLog;
import com.example.labschedulerserver.payload.request.Report.CreateReportRequest;
import com.example.labschedulerserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportMapper {
    private final UserService userService;

    public ReportResponse toReportResponse(Report report, ReportLog reportLog){
        return ReportResponse.builder()
                .id(report.getId())
                .title(report.getTitle())
                .authorContent(report.getContent())
                .status(reportLog.getStatus().name())
                .createdAt(reportLog.getCreatedAt())
                .updatedAt(reportLog.getUpdatedAt())
                .managerContent(reportLog.getContent())
                .build();
    }

    public Report toReport(CreateReportRequest request) {
        return Report.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(userService.getCurrentAccount())
                .build();
    }
}
