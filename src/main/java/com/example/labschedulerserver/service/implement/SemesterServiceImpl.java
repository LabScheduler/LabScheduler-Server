package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Semester;
import com.example.labschedulerserver.payload.response.SemesterResponse;
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
        return semesterRepository.findCurrentSemester(LocalDateTime.now()).get();
    }

    @Override
    public List<SemesterResponse> getAllSemesters() {
        List<Semester> semesters = semesterRepository.findAll();

        List<SemesterResponse> semesterResponses = new ArrayList<>();
        for(Semester semester: semesters){
            SemesterResponse semesterResponse = SemesterResponse.builder()
                    .id(semester.getId())
                    .name(semester.getName())
                    .startDate(semester.getStartDate())
                    .endDate(semester.getEndDate())
                    .semesterWeeks(semester.getSemesterWeeks())
                    .build();
            semesterResponses.add(semesterResponse);
        }
        return semesterResponses;
    }
}
