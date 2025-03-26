package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.RequestStatus;
import com.example.labschedulerserver.model.LecturerRequest;
import com.example.labschedulerserver.model.LecturerRequestLog;
import com.example.labschedulerserver.model.Schedule;
import com.example.labschedulerserver.payload.request.ProcessRequest;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.LecturerRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Schedule schedule = Schedule.builder().build();
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
    public LecturerRequestLog processRequest(ProcessRequest processRequest) {
        return null;
    }

    @Override
    public LecturerRequestLog getRequestLog(Long requestId) {
        return null;
    }

    @Override
    public void cancelRequest(Long requestId) {

    }
}
