package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.model.Schedule;
import com.example.labschedulerserver.repository.ScheduleRepository;
import com.example.labschedulerserver.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;




    @Override
    public List<Schedule> createSchedule(List<Course> courses) {

        return null;
    }
}
