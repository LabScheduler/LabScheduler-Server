package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.Semester;
import com.example.labschedulerserver.model.SemesterWeek;
import com.example.labschedulerserver.repository.SemesterRepository;
import com.example.labschedulerserver.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {
    private final SemesterRepository semesterRepository;
    @Override
    public Semester getCurrentSemester() {
        return semesterRepository.findCurrentSemester().get();
    }

    @Override
    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }

//    @Override
//    public List<SemesterWeek> getAllSemesterWeekInSemester() {
//        return getCurrentSemester().getSemesterWeeks();
//    }

    @Override
    public List<SemesterWeek> getAllSemesterWeekInSemester(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                                              .orElseThrow(()-> new ResourceNotFoundException("Semester not found with id:"+ semesterId ));
        return semester.getSemesterWeeks();
    }

}
