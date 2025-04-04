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
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.EmailSenderService;
import com.example.labschedulerserver.service.LecturerRequestService;
import com.example.labschedulerserver.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
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
    private final CourseRepository courseRepository;
    private final ScheduleRepository scheduleRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final RoomRepository roomRepository;
    private final SemesterWeekRepository semesterWeekRepository;

    @Override
    public LecturerRequestResponse createScheduleRequest(LecturerScheduleRequest request) {
        LecturerRequest lecturerRequest = LecturerRequest.builder()
                .lecturerAccount(lecturerAccountRepository.findById(request.getLecturerId()).orElseThrow(()-> new ResourceNotFoundException("Lecturer Not Found")))
                .course(courseRepository.findById(request.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Course Not Found")))
                .courseSection(courseSectionRepository.findById(request.getCourseSectionId()).orElseThrow(()-> new ResourceNotFoundException("Course Section Not Found")))
                .newRoom(roomRepository.findById(request.getNewRoomId()).orElseThrow(()-> new ResourceNotFoundException("Room Not Found")))
                .newSemesterWeek(semesterWeekRepository.findById(request.getNewSemesterWeekId()).orElseThrow(()-> new ResourceNotFoundException("Semester Week Not Found")))
                .newDayOfWeek(request.getNewDayOfWeek())
                .newStartPeriod(request.getNewStartPeriod())
                .newTotalPeriod(request.getNewTotalPeriod())
                .reason(request.getReason())
                .type(RequestType.valueOf(request.getType()))
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        LecturerRequestLog lecturerRequestLog = LecturerRequestLog.builder()
                .request(lecturerRequest)
                .status(RequestStatus.PENDING)
                .build();

        LecturerRequest newRequest = lecturerRequestRepository.save(lecturerRequest);
        LecturerRequestLog newRequestLog = lecturerRequestLogRepository.save(lecturerRequestLog);

        return LecturerRequestMapper.toResponse(lecturerRequestRepository.save(lecturerRequest), lecturerRequestLogRepository.save(lecturerRequestLog));
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

        LecturerRequestLog existingLog = lecturerRequestLogRepository.findByRequestId(lecturerRequest.getId()).get();

        existingLog.setManagerAccount(managerAccountRepository.findById(request.getManagerId()).orElseThrow(() -> new ResourceNotFoundException("Manager Not Found")));
        existingLog.setStatus(RequestStatus.valueOf(request.getStatus()));
        existingLog.setRepliedAt(new Timestamp(System.currentTimeMillis()));

        if(request.getStatus().equals("APPROVED")) {
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
        else if(request.getStatus().equals("REJECTED")) {
            emailSenderService.sendEmail(lecturerRequest.getLecturerAccount().getAccount().getEmail(),"Notice of your request",
                    "Your request has been rejected.");
        }
        lecturerRequestLogRepository.save(existingLog);
        return LecturerRequestMapper.toResponse(lecturerRequest, existingLog);
    }

    @Override
    public void cancelRequest(Long requestId) {
        LecturerRequest request = lecturerRequestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request Not Found"));
        LecturerRequestLog requestLog = request.getLecturerRequestLog();
        if (request.getLecturerRequestLog().getStatus() == RequestStatus.PENDING) {
            requestLog.setStatus(RequestStatus.CANCELED);
        } else {
            throw new BadRequestException("Cannot cancel a request that is already processed");
        }
    }
}
