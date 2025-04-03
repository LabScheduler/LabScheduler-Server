package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.RequestStatus;
import com.example.labschedulerserver.common.RequestType;
import com.example.labschedulerserver.common.ScheduleType;
import com.example.labschedulerserver.exception.BadRequestException;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.request.CreateScheduleRequest;
import com.example.labschedulerserver.payload.request.LecturerScheduleRequest;
import com.example.labschedulerserver.payload.request.ProcessRequest;
import com.example.labschedulerserver.payload.response.LecturerRequest.LecturerRequestMapper;
import com.example.labschedulerserver.payload.response.LecturerRequest.LecturerRequestResponse;
import com.example.labschedulerserver.repository.LecturerAccountRepository;
import com.example.labschedulerserver.repository.LecturerRequestLogRepository;
import com.example.labschedulerserver.repository.LecturerRequestRepository;
import com.example.labschedulerserver.repository.ManagerAccountRepository;
import com.example.labschedulerserver.service.EmailSenderService;
import com.example.labschedulerserver.service.LecturerRequestService;
import com.example.labschedulerserver.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LecturerRequestServiceImpl implements LecturerRequestService {

    private final LecturerRequestRepository lecturerRequestRepository;
    private final LecturerRequestLogRepository lecturerRequestLogRepository;
    private final LecturerAccountRepository lecturerAccountRepository;
    private final ManagerAccountRepository managerAccountRepository;
    private final ScheduleService scheduleService;
    private final EmailSenderService emailSenderService;

    @Override
    public LecturerRequestResponse createScheduleRequest(LecturerScheduleRequest request) {
        LecturerRequest lecturerRequest = LecturerRequest.builder()
                .lecturerAccount(LecturerAccount.builder()
                        .accountId(request.getLecturerId())
                        .build())
                .course(Course.builder()
                        .id(request.getCourseId())
                        .build())
                .schedule(Schedule.builder()
                        .id(request.getScheduleId())
                        .build())
                .courseSection(CourseSection.builder()
                        .id(request.getCourseSectionId())
                        .build())
                .newRoom(Room.builder()
                        .id(request.getNewRoomId())
                        .build())
                .newSemesterWeek(SemesterWeek.builder()
                        .id(request.getNewSemesterWeekId())
                        .build())
                .newDayOfWeek(request.getNewDayOfWeek())
                .newStartPeriod(request.getNewStartPeriod())
                .newTotalPeriod(request.getNewTotalPeriod())
                .reason(request.getReason())
                .type(RequestType.valueOf(request.getType()))
                .build();

        LecturerRequestLog lecturerRequestLog = LecturerRequestLog.builder()
                .request(lecturerRequest)
                .status(RequestStatus.PENDING)
                .build();

        LecturerRequest newRequest = lecturerRequestRepository.save(lecturerRequest);
        LecturerRequestLog newRequestLog = lecturerRequestLogRepository.save(lecturerRequestLog);

        return LecturerRequestMapper.toResponse(newRequest, newRequestLog);
    }

    @Override
    public List<LecturerRequestResponse> getAllPendingRequests() {
        List<LecturerRequest> lecturerRequest = lecturerRequestRepository.getAllPendingRequests();
        return lecturerRequest.stream()
                .map(request -> {
                    LecturerRequestLog latestLog = request.getLecturerRequestLog();
                    return LecturerRequestMapper.toResponse(request, latestLog);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LecturerRequestResponse> getAllRequests() {
        return lecturerRequestRepository.findAll().stream()
                .map(request -> {
                    LecturerRequestLog latestLog = request.getLecturerRequestLog();
                    return LecturerRequestMapper.toResponse(request, latestLog);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LecturerRequestResponse> getRequestsByLecturerId(Long lecturerId) {
        return lecturerRequestRepository
                .findAllByLecturerAccount(lecturerAccountRepository.findById(lecturerId).orElseThrow(() -> new ResourceNotFoundException("Lecturer Not Found")))
                .stream().map(request -> {
                    LecturerRequestLog latestLog = request.getLecturerRequestLog();
                    return LecturerRequestMapper.toResponse(request, latestLog);
                }).collect(Collectors.toList());
    }

    @Override
    public LecturerRequestResponse getRequestById(Long requestId) {
        LecturerRequest request = lecturerRequestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request Not Found"));
        return LecturerRequestMapper.toResponse(request, request.getLecturerRequestLog());
    }

    @Override
    public LecturerRequestResponse processRequest(ProcessRequest request) {
        LecturerRequest lecturerRequest = lecturerRequestRepository.findById(request.getRequestId()).orElseThrow(() -> new ResourceNotFoundException("Request Not Found"));
        if(lecturerRequest.getLecturerRequestLog().getStatus() != RequestStatus.PENDING) {
            throw new BadRequestException("Request has already been processed");
        }

        LecturerRequestLog lecturerRequestLog = LecturerRequestLog.builder()
                .request(lecturerRequest)
                .managerAccount(managerAccountRepository.findById(request.getManagerId()).orElseThrow(() -> new ResourceNotFoundException("Manager Not Found")))
                .status(RequestStatus.valueOf(request.getStatus()))
                .repliedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        if(request.getStatus().equals(RequestStatus.APPROVED)) {
            CreateScheduleRequest scheduleRequest = CreateScheduleRequest.builder()
                    .courseId(lecturerRequest.getCourse().getId())
                    .courseSectionId(lecturerRequest.getCourseSection().getId())
                    .roomId(lecturerRequest.getNewRoom().getId())
                    .semesterWeekId(lecturerRequest.getNewSemesterWeek().getId())
                    .dayOfWeek(lecturerRequest.getNewDayOfWeek())
                    .startPeriod(lecturerRequest.getNewStartPeriod())
                    .totalPeriod(lecturerRequest.getNewTotalPeriod())
                    .type(ScheduleType.PRACTICE)
                    .build();
            try{
                scheduleService.createSchedule(scheduleRequest);
            } catch (Exception e) {
                throw new BadRequestException("Failed to create schedule: " + e.getMessage());
            }
            emailSenderService.sendEmail(lecturerRequest.getLecturerAccount().getAccount().getEmail(),"Notice of your request",
                    "Your request has been approved. The new schedule has been created.");

        }
        else if(request.getStatus().equals(RequestStatus.REJECTED)) {
            emailSenderService.sendEmail(lecturerRequest.getLecturerAccount().getAccount().getEmail(),"Notice of your request",
                    "Your request has been rejected.");
        }
        return LecturerRequestMapper.toResponse(lecturerRequest, lecturerRequestLog);
    }

    @Override
    public void cancelRequest(Long requestId) {
        LecturerRequest request = lecturerRequestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request Not Found"));
        if (request.getLecturerRequestLog().getStatus() == RequestStatus.PENDING) {
            lecturerRequestRepository.delete(request);
        } else {
            throw new BadRequestException("Cannot cancel a request that is already processed");
        }
    }
}
