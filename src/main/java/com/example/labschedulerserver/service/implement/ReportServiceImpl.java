package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.ReportStatus;
import com.example.labschedulerserver.exception.BadRequestException;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.model.ManagerAccount;
import com.example.labschedulerserver.model.Report;
import com.example.labschedulerserver.model.ReportLog;
import com.example.labschedulerserver.payload.request.Report.CreateReportRequest;
import com.example.labschedulerserver.payload.request.Report.UpdateReportRequest;
import com.example.labschedulerserver.payload.response.Report.ReportMapper;
import com.example.labschedulerserver.payload.response.Report.ReportResponse;
import com.example.labschedulerserver.repository.ReportLogRepository;
import com.example.labschedulerserver.repository.ReportRepository;
import com.example.labschedulerserver.service.ReportService;
import com.example.labschedulerserver.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportLogRepository reportLogRepository;
    private final ReportMapper reportMapper;
    private final UserService userService;

    @Override
    public ReportResponse createReport(CreateReportRequest request) {
        Report report = Report.builder()
                .author(userService.getCurrentAccount())
                .content(request.getContent())
                .title(request.getTitle())
                .build();

        ReportLog reportLog = ReportLog.builder()
                .status(ReportStatus.PENDING)
                .report(report)
                .manager(null)
                .build();
        return reportMapper.toReportResponse(reportRepository.save(report), reportLogRepository.save(reportLog));
    }

    @Override
    public ReportResponse getReportById(Long reportId) {
        return reportMapper.toReportResponse(reportRepository.findById(reportId).orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId)), null);
    }

    @Override
    public List<ReportResponse> getReportByUserId(Long userId) {
        return reportRepository.findAllByAuthorId(userId).stream()
                .map(report -> {
                    ReportLog reportLog = report.getReportLog();
                    return reportMapper.toReportResponse(report, reportLog);
                })
                .toList();
    }

    @Override
    public List<ReportResponse> getAllReport() {
        return reportRepository.findAll().stream()
                .map(report -> {
                    ReportLog reportLog = report.getReportLog();
                    return reportMapper.toReportResponse(report, reportLog);
                })
                .toList();
    }

    @Override
    public List<ReportResponse> getPendingReports() {
        return getAllReport().stream()
                .filter(reportResponse -> {
                    return reportResponse.getStatus().equals("PENDING");
                })
                .toList();
    }

    @Override
    public ReportResponse processReport(Long reportId, String status) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
        if (report.getReportLog().getStatus() != ReportStatus.PENDING)
            throw new BadRequestException("Report is not pending, can not process");
        ReportLog reportLog = report.getReportLog();
        reportLog.setStatus(ReportStatus.valueOf(status.toUpperCase()));
        reportLog.setManager((ManagerAccount) userService.getAccountInfo(userService.getCurrentAccount()));

        return reportMapper.toReportResponse(reportRepository.save(report), reportLogRepository.save(reportLog));
    }

    @Override
    public void deleteReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
        reportRepository.delete(report);
    }

    @Override
    public ReportResponse updateReport(Long reportId, UpdateReportRequest request) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
        if (report.getReportLog().getStatus() != ReportStatus.PENDING)
            throw new BadRequestException("Report is not pending, can not update");

        report.setTitle(request.getTitle());
        report.setContent(request.getContent());
        return reportMapper.toReportResponse(reportRepository.save(report), report.getReportLog());
    }

    @Override
    public ReportResponse cancelReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
        if (report.getReportLog().getStatus() != ReportStatus.PENDING)
            throw new BadRequestException("Report is not pending, can not cancel");
        report.getReportLog().setStatus(ReportStatus.CANCELLED);
        report.getReportLog().setContent("Báo cáo đã bị hủy bởi người dùng");
        return reportMapper.toReportResponse(reportRepository.save(report), reportLogRepository.save(report.getReportLog()));
    }
}