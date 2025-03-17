package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Semester;
import com.example.labschedulerserver.repository.SemesterRepository;
import com.example.labschedulerserver.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {
    private final SemesterRepository semesterRepository;
    @Override
    public Semester getCurrentSemester() {
        return semesterRepository.findCurrentSemester(LocalDateTime.now()).get();
    }
}
