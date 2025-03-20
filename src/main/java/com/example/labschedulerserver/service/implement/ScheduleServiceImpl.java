package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.repository.CourseRepository;
import com.example.labschedulerserver.repository.RoomRepository;
import com.example.labschedulerserver.repository.ScheduleRepository;
import com.example.labschedulerserver.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final CourseRepository courseRepository;
    private final RoomRepository roomRepository;


    @Override
    public List<Schedule> getAllScheduleInSemester(Long semesterId) {
        return List.of();
    }

    @Override
    public List<Schedule> getAllSchedulesInSpecificWeek(Integer weekId) {
        return List.of();
    }

    @Override
    public List<Schedule> allocateSchedule(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(()->new RuntimeException("Course not found with id: "+courseId));
        List<CourseSection> courseSections = course.getCourseSections().stream().filter(section-> section.getSectionNumber()>0).toList();
        List<Room> rooms = roomRepository.findAll();
        List<SemesterWeek> semesterWeeks = course.getSemester().getSemesterWeeks();


        Map<Long,List<Schedule>> lecturerSchedule;



        return null;
    }
}
