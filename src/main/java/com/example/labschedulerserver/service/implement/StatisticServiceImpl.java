package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.model.Room;
import com.example.labschedulerserver.payload.response.StatisticsResponse;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.LecturerRequestService;
import com.example.labschedulerserver.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final RoomRepository roomRepository;
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final ScheduleRepository scheduleRepository;
    private final AccountRepository accountRepository;
    private final LecturerRequestService lecturerRequestService;

    @Override
    public StatisticsResponse getStatistics() {
        List<Room> rooms = roomRepository.findAll();

        long availableRoom = rooms.stream()
                .filter(room -> room.getStatus().toString().equals("AVAILABLE"))
                .count();

        long repairingRoom = rooms.stream()
                .filter(room -> room.getStatus().toString().equals("REPAIRING"))
                .count();

        List<Account> accounts = accountRepository.findAll();

        long totalStudents = accounts.stream().filter(account -> {
            return account.getRole().getName().equals("STUDENT");
        }).toList().size();

        long totalLecturers = accounts.stream().filter(account -> {
            return account.getRole().getName().equals("LECTURER");
        }).toList().size();


        return StatisticsResponse.builder()
                .totalRooms(availableRoom + repairingRoom)
                .totalAvailableRooms(availableRoom)
                .totalUnavailableRooms(rooms.size() - availableRoom - repairingRoom)
                .totalRepairingRooms(repairingRoom)
                .totalCoursesInSemester(courseRepository.findAllBySemesterId(semesterRepository.findCurrentSemester().get().getId()).size())
                .totalPracticeSchedulesInSemester(scheduleRepository.findAllByCurrentSemester().size())
                .totalLecturers(totalLecturers)
                .totalStudents(totalStudents)
                .totalPendingRequests(lecturerRequestService.getAllPendingRequests().size())
                .build();
    }
}
