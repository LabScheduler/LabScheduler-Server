package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.exception.BadRequestException;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.Semester;
import com.example.labschedulerserver.model.SemesterWeek;
import com.example.labschedulerserver.payload.request.CreateSemesterRequest;
import com.example.labschedulerserver.repository.SemesterRepository;
import com.example.labschedulerserver.repository.SemesterWeekRepository;
import com.example.labschedulerserver.service.SemesterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {
    private final SemesterRepository semesterRepository;
    private final SemesterWeekRepository semesterWeekRepository;

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
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found with id:" + semesterId));
        return semester.getSemesterWeeks();
    }

    @Override
    @Transactional
    public Semester createSemester(CreateSemesterRequest request) {
        Semester semester = Semester.builder()
                .code(request.getCode())
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        semester = semesterRepository.save(semester);
        List<SemesterWeek> semesterWeeks = generateSemesterWeeks(semester, request.getStartDate(), request.getEndDate(), request.getStartWeek());
        semesterWeekRepository.saveAll(semesterWeeks);
        return semester;
    }

    private List<SemesterWeek> generateSemesterWeeks(Semester semester, LocalDate startDate, LocalDate endDate, int startWeek) {
        List<SemesterWeek> semesterWeeks = new ArrayList<>();
        LocalDate currentDate = startDate;

        int weekNumber = startWeek;
        while (currentDate.isBefore(endDate)) {
            SemesterWeek semesterWeek = SemesterWeek.builder()
                    .name("Tuần " + weekNumber)
                    .startDate(Optional.of(currentDate)
                            .filter(date -> date.getDayOfWeek() == DayOfWeek.MONDAY)
                            .orElseThrow(() -> new BadRequestException("Start date must be Monday")))
                    .endDate(currentDate.plusDays(6))
                    .semester(semester)
                    .build();
            semesterWeeks.add(semesterWeek);
            currentDate = currentDate.plusWeeks(1);
            weekNumber++;
        }

        return semesterWeeks;
    }

}
