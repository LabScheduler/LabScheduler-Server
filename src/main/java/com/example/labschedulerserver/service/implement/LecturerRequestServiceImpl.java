package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.RequestStatus;
import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.exception.ScheduleException;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.request.LecturerScheduleRequest;
import com.example.labschedulerserver.payload.request.ProcessRequest;
import com.example.labschedulerserver.payload.response.LecturerRequest.LecturerRequestMapper;
import com.example.labschedulerserver.payload.response.LecturerRequest.LecturerRequestResponse;
import com.example.labschedulerserver.payload.response.Schedule.ScheduleMapper;
import com.example.labschedulerserver.payload.response.Schedule.ScheduleResponse;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.EmailSenderService;
import com.example.labschedulerserver.service.LecturerRequestService;
import com.example.labschedulerserver.service.ScheduleService;
import com.example.labschedulerserver.service.UserService;
import com.example.labschedulerserver.utils.ScheduleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LecturerRequestServiceImpl implements LecturerRequestService {
    private final LecturerRequestRepository lecturerRequestRepository;
    private final LecturerRequestLogRepository lecturerRequestLogRepository;
    private final UserService userService;
    private final ManagerAccountRepository managerAccountRepository;
    private final CourseRepository courseRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final RoomRepository roomRepository;
    private final LecturerAccountRepository lecturerAccountRepository;
    private final SemesterRepository semesterRepository;
    private final SemesterWeekRepository semesterWeekRepository;
    private final ScheduleUtils scheduleUtils;
    private final ScheduleRepository scheduleRepository;
    private final EmailSenderService emailSenderService;
    private final ScheduleService scheduleService;

    @Override
    public LecturerRequestResponse createScheduleRequest(LecturerScheduleRequest request) {
        Account account = userService.getCurrentAccount();

        LecturerAccount lecturerAccount = (LecturerAccount) lecturerAccountRepository.findByAccount(account);

        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        CourseSection courseSection = courseSectionRepository.findById(request.getCourseSectionId()).orElseThrow(() -> new ResourceNotFoundException("Course section not found with id: " + request.getCourseSectionId()));

        Room room = roomRepository.findById(request.getNewRoomId()).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getNewRoomId()));

        SemesterWeek semesterWeek = semesterWeekRepository.findById(request.getNewSemesterWeekId()).orElseThrow(() -> new ResourceNotFoundException("Semester week not found with id: " + request.getNewSemesterWeekId()));

        Schedule schedule = Schedule.builder()
                .course(course)
                .courseSection(courseSection)
                .room(room)
                .lecturer(lecturerAccount)
                .semesterWeek(semesterWeek)
                .dayOfWeek(request.getNewDayOfWeek())
                .startPeriod(request.getNewStartPeriod())
                .totalPeriod(request.getNewTotalPeriod())
                .build();

        Schedule conflict = scheduleUtils.checkScheduleConflict(schedule, scheduleRepository.findAllByCurrentSemester());

        if (conflict != null) {
//            throw new ScheduleException("Your request has conflict with: ", ScheduleMapper.mapScheduleToResponse(conflict));
            return null;
        }

        LecturerRequest lecturerRequest = LecturerRequest.builder()
                .lecturerAccount((LecturerAccount) lecturerAccountRepository.findByAccount(userService.getCurrentAccount()))
                .body(request.getBody())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .course(course)
                .room(room)
                .courseSection(courseSection)
                .semesterWeek(semesterWeek)
                .startPeriod(request.getNewStartPeriod())
                .totalPeriod(request.getNewTotalPeriod())
                .dayOfWeek(request.getNewDayOfWeek())
                .build();

        LecturerRequestLog log = LecturerRequestLog.builder()
                .status(RequestStatus.PENDING)
                .request(lecturerRequest)
                .managerAccount(null)
                .build();

        return LecturerRequestMapper.toResponse(lecturerRequestRepository.save(lecturerRequest), lecturerRequestLogRepository.save(log));
    }

    @Override
    public List<LecturerRequestResponse> getAllPendingRequests() {
        return lecturerRequestRepository.getAllPendingRequests()
                .stream()
                .map(request -> {
                    return LecturerRequestMapper.toResponse(request, request.getLecturerRequestLog());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LecturerRequestResponse> getAllRequests() {
        return lecturerRequestRepository.findAll()
                .stream()
                .map(request -> {
                    return LecturerRequestMapper.toResponse(request, request.getLecturerRequestLog());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LecturerRequestResponse> getRequestsByLecturer() {
        Account account = userService.getCurrentAccount();
        LecturerAccount lecturerAccount = (LecturerAccount) lecturerAccountRepository.findByAccount(account);

        return lecturerRequestRepository.findAllByLecturerAccount(lecturerAccount)
                .stream()
                .map(request -> {
                    return LecturerRequestMapper.toResponse(request, request.getLecturerRequestLog());
                })
                .collect(Collectors.toList());
    }


    @Override
    public LecturerRequestResponse processRequest(ProcessRequest request) {
        LecturerRequest lecturerRequest = lecturerRequestRepository.findById(request.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + request.getRequestId()));
        LecturerRequestLog lecturerRequestLog = lecturerRequest.getLecturerRequestLog();

        lecturerRequestLog.setBody(request.getBody());
        lecturerRequestLog.setStatus(RequestStatus.valueOf(request.getStatus().toUpperCase()));
        lecturerRequestLog.setManagerAccount(managerAccountRepository.findByAccount(userService.getCurrentAccount()));

        if (lecturerRequestLog.getStatus() == RequestStatus.APPROVED) {
            Schedule schedule = Schedule.builder()
                    .course(lecturerRequest.getCourse())
                    .courseSection(lecturerRequest.getCourseSection())
                    .room(lecturerRequest.getRoom())
                    .lecturer(lecturerRequest.getLecturerAccount())
                    .semesterWeek(lecturerRequest.getSemesterWeek())
                    .startPeriod(lecturerRequest.getStartPeriod())
                    .totalPeriod(lecturerRequest.getTotalPeriod())
                    .dayOfWeek(lecturerRequest.getDayOfWeek())
                    .status(ScheduleStatus.IN_PROGRESS)
                    .build();

            Schedule conflict = scheduleUtils.checkScheduleConflict(schedule, scheduleRepository.findAllByCurrentSemester());
            if (conflict != null) {
                throw new ScheduleException("Your request has conflict with: ", ScheduleMapper.mapScheduleToResponse(conflict));
            }
            scheduleRepository.save(schedule);
        } else if (lecturerRequestLog.getStatus() == RequestStatus.REJECTED) {
            emailSenderService.sendEmail(lecturerRequest.getLecturerAccount().getEmail(), "Yêu cầu bị từ chối",
                    "\n\nYêu cầu của bạn đã bị từ chối bởi: " + userService.getCurrentManager().getFullName() +
                            "\n\nLý do: " + lecturerRequestLog.getBody() +
                            "\n\nXin hãy gửi lại yêu cầu hoặc trao đổi trực tiếp với quản lý.");
        }

        return LecturerRequestMapper.toResponse(lecturerRequest, lecturerRequestLogRepository.save(lecturerRequestLog));
    }

    @Override
    public void cancelRequest(Long requestId) {
        Account account = userService.getCurrentAccount();
        LecturerAccount lecturerAccount = (LecturerAccount) lecturerAccountRepository.findByAccount(account);

        LecturerRequest lecturerRequest = lecturerRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));
        if (lecturerRequest.getLecturerRequestLog().getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request cannot be cancelled because it has been processed");
        } else {
            LecturerRequestLog lecturerRequestLog = lecturerRequest.getLecturerRequestLog();
            lecturerRequestLog.setStatus(RequestStatus.CANCELLED);

            lecturerRequestLog.setBody("Request has been cancelled by: " + lecturerAccount.getFullName());
            lecturerRequestLog.setManagerAccount(managerAccountRepository.findByAccount(userService.getCurrentAccount()));
            lecturerRequestLogRepository.save(lecturerRequestLog);
        }
    }

    @Override
    public ScheduleResponse checkScheduleConflict(LecturerScheduleRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        CourseSection courseSection = courseSectionRepository.findById(request.getCourseSectionId()).orElseThrow(() -> new ResourceNotFoundException("Course section not found with id: " + request.getCourseSectionId()));

        Room room = roomRepository.findById(request.getNewRoomId()).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getNewRoomId()));

        SemesterWeek semesterWeek = semesterWeekRepository.findById(request.getNewSemesterWeekId()).orElseThrow(() -> new ResourceNotFoundException("Semester week not found with id: " + request.getNewSemesterWeekId()));

        Schedule schedule = Schedule.builder()
                .course(course)
                .courseSection(courseSection)
                .room(room)
                .semesterWeek(semesterWeek)
                .dayOfWeek(request.getNewDayOfWeek())
                .startPeriod(request.getNewStartPeriod())
                .totalPeriod(request.getNewTotalPeriod())
                .lecturer((LecturerAccount) lecturerAccountRepository.findByAccount(userService.getCurrentAccount()))
                .build();
        Schedule conflict = scheduleUtils.checkScheduleConflict(schedule, scheduleRepository.findAllByCurrentSemester());
        if (conflict != null) {
            return ScheduleMapper.mapScheduleToResponse(conflict);
        }
        return null;
    }

}
