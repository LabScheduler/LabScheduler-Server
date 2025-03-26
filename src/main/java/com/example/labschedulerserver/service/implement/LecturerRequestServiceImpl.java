package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.RequestStatus;
import com.example.labschedulerserver.common.RequestType;
import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.LecturerRequestService;
import com.example.labschedulerserver.ultils.RequestUtils;
import com.example.labschedulerserver.ultils.ScheduleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

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
    public LecturerRequest createScheduleRequest(LecturerRequest request) {
        return null;
    }

    @Override
    public List<LecturerRequest> getAllPendingRequests() {
        return List.of();
    }

    @Override
    public List<LecturerRequest> getAllRequests() {
        return List.of();
    }

    @Override
    public List<LecturerRequest> getRequestsByLecturerId(Long lecturerId) {
        return List.of();
    }

    @Override
    public LecturerRequest getRequestById(Long requestId) {
        return null;
    }

    @Override
    public LecturerRequestLog processRequest(Long requestId, Long managerId, RequestStatus status) {
        return null;
    }

    @Override
    public LecturerRequestLog getRequestLogByRequestId(Long requestId) {
        return null;
    }

    @Override
    public void cancelRequest(Long requestId) {

    }

    @Override
    public Schedule addScheduleFromRequest(LecturerRequest request) {
        return null;
    }

    @Override
    public Schedule rescheduleFromRequest(LecturerRequest request) {
        return null;
    }
}
