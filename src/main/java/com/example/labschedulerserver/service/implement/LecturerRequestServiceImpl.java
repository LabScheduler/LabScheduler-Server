package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.RequestStatus;
import com.example.labschedulerserver.common.RequestType;
import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.request.LecturerScheduleRequest;
import com.example.labschedulerserver.payload.request.ProcessRequest;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.LecturerRequestService;
import com.example.labschedulerserver.utils.RequestUtils;
import com.example.labschedulerserver.utils.ScheduleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LecturerRequestServiceImpl implements LecturerRequestService {
    private final ScheduleRepository scheduleRepository;
    private final LecturerRequestRepository lecturerRequestRepository;
    private final LecturerRequestLogRepository lecturerRequestLogRepository;
    private final LecturerAccountRepository lecturerAccountRepository;
    private final ManagerAccountRepository managerAccountRepository;
    private final RoomRepository roomRepository;
    private final SemesterWeekRepository semesterWeekRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public LecturerRequest createScheduleRequest(LecturerScheduleRequest requestPayload) {
        LecturerAccount lecturer = lecturerAccountRepository.findById(requestPayload.getLecturerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer not found"));
                
        LecturerRequest request = buildLecturerRequest(requestPayload, lecturer);
                
        if (request.getType() == RequestType.ADD_SCHEDULE) {
            validateAddScheduleRequest(request, lecturer);
        } else if (request.getType() == RequestType.RESCHEDULE) {
            validateRescheduleRequest(request, lecturer);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request type");
        }
        
        return lecturerRequestRepository.save(request);
    }
    
    private LecturerRequest buildLecturerRequest(LecturerScheduleRequest payload, LecturerAccount lecturer) {
        LecturerRequest request = new LecturerRequest();
        request.setLecturerAccount(lecturer);
        
        if (payload.getScheduleId() != null) {
            Schedule schedule = new Schedule();
            schedule.setId(payload.getScheduleId());
            request.setSchedule(schedule);
        }
        
        if (payload.getCourseId() != null) {
            Course course = new Course();
            course.setId(payload.getCourseId());
            request.setCourse(course);
        }
        
        if (payload.getRoomId() != null) {
            Room room = new Room();
            room.setId(payload.getRoomId());
            request.setNewRoom(room);
        }
        
        if (payload.getSemesterWeekId() != null) {
            SemesterWeek semesterWeek = new SemesterWeek();
            semesterWeek.setId(payload.getSemesterWeekId());
            request.setNewSemesterWeek(semesterWeek);
        }
        
        request.setNewDayOfWeek(payload.getNewDayOfWeek());
        request.setNewStartPeriod(payload.getNewStartPeriod());
        request.setNewTotalPeriod(payload.getNewTotalPeriod());
        request.setReason(payload.getReason());
        
        try {
            request.setType(RequestType.valueOf(payload.getType().toUpperCase()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request type. Must be ADD_SCHEDULE or RESCHEDULE");
        }
        
        return request;
    }

    private void validateAddScheduleRequest(LecturerRequest request, LecturerAccount lecturer) {
        Course course = courseRepository.findById(request.getCourse().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        request.setCourse(course);
        
        if (!RequestUtils.isLecturerTeachingCourse(course, lecturer)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lecturer not assigned to this course");
        }
        
        if (!RequestUtils.hasValidReason(request)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reason is required");
        }
        
        validateScheduleParameters(request, null, course);
    }

    private void validateRescheduleRequest(LecturerRequest request, LecturerAccount lecturer) {
        Schedule schedule = scheduleRepository.findById(request.getSchedule().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));
        request.setSchedule(schedule);
        
        if (!RequestUtils.isLecturerTeachingSchedule(schedule, lecturer)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Lecturer not assigned to this schedule");
        }
        
        if (!RequestUtils.hasValidReason(request)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reason is required");
        }
        
        if (!RequestUtils.hasRescheduleChanges(request)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No schedule changes specified");
        }
        
        Course course = schedule.getCourseSection().getCourse();
        validateScheduleParameters(request, schedule.getId(), course);
    }

    private void validateScheduleParameters(LecturerRequest request, Long excludedScheduleId, Course course) {
        byte dayOfWeek = request.getNewDayOfWeek() != null ? request.getNewDayOfWeek() : 0;
        byte startPeriod = request.getNewStartPeriod() != null ? request.getNewStartPeriod() : 0;
        byte totalPeriod = request.getNewTotalPeriod() != null ? request.getNewTotalPeriod() : 0;
        
        if (!ScheduleUtils.isValidDayOfWeek(dayOfWeek)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid day of week");
        }
        
        if (!ScheduleUtils.isValidPeriod(startPeriod)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid start period");
        }
        
        if (!ScheduleUtils.isValidTotalPeriods(startPeriod, totalPeriod)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid total periods");
        }
        
        Room room = null;
        if (request.getNewRoom() != null) {
            room = roomRepository.findById(request.getNewRoom().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
            request.setNewRoom(room);
        }
        
        SemesterWeek week = null;
        if (request.getNewSemesterWeek() != null) {
            week = semesterWeekRepository.findById(request.getNewSemesterWeek().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Semester week not found"));
            request.setNewSemesterWeek(week);
        }
        
        if (room == null || week == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room and semester week are required");
        }
        
        List<Schedule> existingSchedules = scheduleRepository.findAll();
        
        if (ScheduleUtils.hasScheduleConflict(existingSchedules, week, dayOfWeek, startPeriod, totalPeriod, room, course, excludedScheduleId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Schedule conflicts with existing schedules");
        }
    }

    @Override
    public List<LecturerRequest> getAllPendingRequests() {
        return lecturerRequestRepository.findByLecturerRequestLogIsNull();
    }

    @Override
    public List<LecturerRequest> getAllRequests() {
        return lecturerRequestRepository.findAll();
    }

    @Override
    public List<LecturerRequest> getRequestsByLecturerId(Long lecturerId) {
        LecturerAccount lecturer = lecturerAccountRepository.findById(lecturerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer not found"));
        
        return lecturerRequestRepository.findByLecturerAccountId(lecturerId);
    }

    @Override
    public LecturerRequest getRequestById(Long requestId) {
        return lecturerRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
    }

    @Override
    @Transactional
    public LecturerRequestLog processRequest(ProcessRequest processRequest) {
        LecturerRequest request = lecturerRequestRepository.findById(processRequest.getRequestId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        
        ManagerAccount manager = managerAccountRepository.findById(processRequest.getManagerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found"));
        
        if (request.getLecturerRequestLog() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request already processed");
        }
        
        RequestStatus status;
        try {
            status = RequestStatus.valueOf(processRequest.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status. Must be APPROVED or REJECTED");
        }
        
        LecturerRequestLog log = LecturerRequestLog.builder()
                .request(request)
                .managerAccount(manager)
                .status(status)
                .build();
        
        log = lecturerRequestLogRepository.save(log);
        
        if (status == RequestStatus.APPROVED) {
            if (request.getType() == RequestType.ADD_SCHEDULE) {
                createNewSchedule(request);
            } else if (request.getType() == RequestType.RESCHEDULE) {
                updateExistingSchedule(request);
            }
        }
        
        return log;
    }

    private void createNewSchedule(LecturerRequest request) {
        Course course = courseRepository.findById(request.getCourse().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        
        CourseSection courseSection = createOrGetCourseSection(course);
        courseSection = courseSectionRepository.save(courseSection);
        
        Schedule schedule = Schedule.builder()
                .courseSection(courseSection)
                .room(request.getNewRoom())
                .dayOfWeek(request.getNewDayOfWeek())
                .startPeriod(request.getNewStartPeriod())
                .totalPeriod(request.getNewTotalPeriod())
                .semesterWeek(request.getNewSemesterWeek())
                .scheduleType(determineScheduleType(course))
                .scheduleStatus(ScheduleStatus.IN_PROGRESS)
                .build();
        
        scheduleRepository.save(schedule);
    }
    
    private CourseSection createOrGetCourseSection(Course course) {
        List<CourseSection> sections = course.getCourseSections();
        
        if (sections == null || sections.isEmpty()) {
            return CourseSection.builder()
                    .course(course)
                    .sectionNumber(1)
                    .totalStudentsInSection(course.getTotalStudents())
                    .build();
        }
        
        int maxSectionNumber = sections.stream()
                .mapToInt(CourseSection::getSectionNumber)
                .max()
                .orElse(0);
        
        return CourseSection.builder()
                .course(course)
                .sectionNumber(maxSectionNumber + 1)
                .totalStudentsInSection(course.getTotalStudents())
                .build();
    }
    
    private ScheduleType determineScheduleType(Course course) {
        return course.getSubject().getTotalPracticePeriods() > 0 
                ? ScheduleType.PRACTICE 
                : ScheduleType.THEORY;
    }

    private void updateExistingSchedule(LecturerRequest request) {
        Schedule schedule = scheduleRepository.findById(request.getSchedule().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));
        
        Optional.ofNullable(request.getNewRoom()).ifPresent(schedule::setRoom);
        Optional.ofNullable(request.getNewSemesterWeek()).ifPresent(schedule::setSemesterWeek);
        Optional.ofNullable(request.getNewDayOfWeek()).ifPresent(schedule::setDayOfWeek);
        Optional.ofNullable(request.getNewStartPeriod()).ifPresent(schedule::setStartPeriod);
        Optional.ofNullable(request.getNewTotalPeriod()).ifPresent(schedule::setTotalPeriod);
        
        scheduleRepository.save(schedule);
    }

    @Override
    public LecturerRequestLog getRequestLog(Long requestId) {
        LecturerRequest request = lecturerRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        
        if (request.getLecturerRequestLog() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request log not found");
        }
        
        return request.getLecturerRequestLog();
    }

    @Override
    @Transactional
    public void cancelRequest(Long requestId) {
        LecturerRequest request = lecturerRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        
        if (request.getLecturerRequestLog() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cancel processed request");
        }
        
        lecturerRequestRepository.delete(request);
    }
}
