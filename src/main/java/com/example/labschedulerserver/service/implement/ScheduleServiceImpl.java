package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.RoomStatus;
import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.repository.CourseRepository;
import com.example.labschedulerserver.repository.RoomRepository;
import com.example.labschedulerserver.repository.ScheduleRepository;
import com.example.labschedulerserver.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final CourseRepository courseRepository;
    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public List<Schedule> allocateSchedule(Long courseId) {
        return null;
    }

    @Override
    public List<Schedule> getAllScheduleInSemester(Long semesterId) {
        return null;
    }

    @Override
    public List<Schedule> getAllSchedulesInSpecificWeek(Integer weekId) {
        return null;
    }
}
