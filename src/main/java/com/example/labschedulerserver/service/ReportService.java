package com.example.labschedulerserver.service;

import com.example.labschedulerserver.payload.request.Report.CreateReportRequest;
import com.example.labschedulerserver.payload.request.Report.UpdateReportRequest;
import com.example.labschedulerserver.payload.response.Report.ReportResponse;

import java.util.List;

public interface ReportService {
    ReportResponse createReport(CreateReportRequest request);
    ReportResponse getReportById(Long reportId);
    List<ReportResponse> getReportByUserId(Long userId);
    List<ReportResponse> getAllReport();
    List<ReportResponse> getPendingReports();
    void deleteReport(Long reportId);
    ReportResponse updateReport(Long reportId, UpdateReportRequest request);
    ReportResponse cancelReport(Long reportId);
}
