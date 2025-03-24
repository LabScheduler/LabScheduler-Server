package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.ManagerRequest;
import com.example.labschedulerserver.model.Schedule;
import com.example.labschedulerserver.repository.ScheduleRepository;
import com.example.labschedulerserver.service.ManagerRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerRequestServiceImpl implements ManagerRequestService {
    private final ScheduleRepository scheduleRepository;

    @Override
    public Schedule addSchedule(ManagerRequest request) {

        return null;
    }

    @Override
    public Schedule reSchedule(ManagerRequest request) {
        Schedule schedule = scheduleRepository.findById(request.getSchedule().getId()).orElseThrow(()-> new RuntimeException("Schedule not found"));


        return null;
    }
}
