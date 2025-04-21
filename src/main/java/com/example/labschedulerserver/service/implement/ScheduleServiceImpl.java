package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.RoomStatus;
import com.example.labschedulerserver.common.ScheduleStatus;
import com.example.labschedulerserver.common.ScheduleType;
import com.example.labschedulerserver.exception.BadRequestException;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.request.CreateScheduleRequest;
import com.example.labschedulerserver.payload.request.UpdateScheduleRequest;
import com.example.labschedulerserver.payload.response.Schedule.ScheduleMapper;
import com.example.labschedulerserver.payload.response.Schedule.ScheduleResponse;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.ScheduleService;
import com.example.labschedulerserver.utils.ScheduleSlot;
import com.example.labschedulerserver.utils.ScheduleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final CourseRepository courseRepository;
    private final CourseSectionRepository courseSectionRepository;
    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;
    private final SemesterWeekRepository semesterWeekRepository;
    private final SemesterRepository semesterRepository;
    private final ClassRepository classRepository;
    private final LecturerAccountRepository lecturerAccountRepository;

    @Override
    @Transactional
    public List<ScheduleResponse> allocateSchedule(Long courseId) {
        //Check if course exists
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        //check if course has no practice periods
        Subject subject = course.getSubject();
        if (subject.getTotalPracticePeriods() <= 0) {
            throw new BadRequestException("This course does not have practice sessions");
        }

        //get practice sections
        List<CourseSection> sections = courseSectionRepository.findAllByCourseId(courseId);
        if (sections.isEmpty()) {
            throw new BadRequestException("No sections found for this course");
        }


        //get all existing schedules
        List<Schedule> existingSchedules = scheduleRepository.findAllByCurrentSemester();

        //get all semester weeks in current semester
        List<SemesterWeek> semesterWeeks = semesterWeekRepository.findBySemesterId(course.getSemester().getId());

        //sort semester weeks by start date
        semesterWeeks.sort(Comparator.comparing(SemesterWeek::getStartDate));

        //get available rooms
        List<Room> allRooms = roomRepository.findAllByStatus(RoomStatus.AVAILABLE).stream().toList();

        //Filter available rooms by capacity
        int maxSectionSize = sections.stream()
                .mapToInt(CourseSection::getTotalStudentsInSection)
                .max()
                .orElseThrow(() -> new RuntimeException("Error"));
        List<Room> availableRooms = ScheduleUtils.filterRoomsByCapacity(allRooms, maxSectionSize);

        if (availableRooms.isEmpty()) {
            throw new BadRequestException("No rooms available with sufficient capacity");
        }

        //generate practice schedules
        List<Schedule> createdSchedules = generateSequentialPracticeSchedules(
                course, sections, availableRooms, existingSchedules, semesterWeeks, subject.getTotalPracticePeriods());

        //map to schedule response
        return createdSchedules.stream()
                .map(ScheduleMapper::mapScheduleToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponse> getAllScheduleInSemester() {
        List<Schedule> schedules = scheduleRepository.findAllByCurrentSemester();
        return schedules.stream()
                .map(ScheduleMapper::mapScheduleToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponse> getAllScheduleBySemesterId(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found with id: " + semesterId));
        List<Schedule> schedules = scheduleRepository.findAllBySemesterId(semesterId);
        return schedules.stream()
                .map(ScheduleMapper::mapScheduleToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponse> getAllScheduleByClassId(Long classId) {
        Clazz clazz = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));
        List<Schedule> schedules = scheduleRepository.findAllByClassIdInCurrentSemester(classId);
        return schedules.stream()
                .map(ScheduleMapper::mapScheduleToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponse> getAllScheduleByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        List<Schedule> schedules = scheduleRepository.findAllByCourseId(courseId);
        return schedules.stream()
                .map(ScheduleMapper::mapScheduleToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponse> getAllScheduleByLecturerId(Long lecturerId) {
        LecturerAccount lecturer = lecturerAccountRepository.findById(lecturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with id: " + lecturerId));

        List<Schedule> schedules = scheduleRepository.findAllByLecturerIdInCurrentSemester(lecturerId);
        return schedules.stream()
                .map(ScheduleMapper::mapScheduleToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponse> getAllSchedulesInSpecificWeek(Long weekId) {
        List<Schedule> schedules = scheduleRepository.findAllByWeekId(weekId);
        return schedules.stream()
                .map(ScheduleMapper::mapScheduleToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponse updateSchedule(UpdateScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + request.getScheduleId()));

        schedule.setRoom(roomRepository.findById(request.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId())));
        schedule.setScheduleStatus(ScheduleStatus.valueOf(request.getStatus()));
        return ScheduleMapper.mapScheduleToResponse(scheduleRepository.save(schedule));
    }

    @Override
    public List<ScheduleResponse> filterSchedule(Long semesterId, Long classId, Long courseId, Long lecturerId) {
        List<Schedule> schedules = scheduleRepository.findAllBySemesterId(semesterId);
        if (classId != null) {
            schedules = schedules.stream()
                    .filter(schedule -> schedule.getCourse().getClazz().getId().equals(classId))
                    .toList();
        }
        if (courseId != null) {
            schedules = schedules.stream()
                    .filter(schedule -> schedule.getCourse().getId().equals(courseId))
                    .toList();
        }
        if (lecturerId != null) {
            schedules = schedules.stream()
                    .filter(schedule -> schedule.getCourse().getLecturerAccount().getAccountId().equals(lecturerId))
                    .toList();
        }

        return schedules.stream().map(ScheduleMapper::mapScheduleToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ScheduleResponse createSchedule(CreateScheduleRequest request) {
        //Get course
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        //Get room
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        //Get semester week
        SemesterWeek semesterWeek = semesterWeekRepository.findById(request.getSemesterWeekId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester week not found"));

        //Get a course section
        CourseSection courseSection = null;
        if (request.getCourseSectionId() != null) {
            courseSection = courseSectionRepository.findById(request.getCourseSectionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course section not found"));
        }

        //Validate schedule param
        if (!ScheduleUtils.isValidDayOfWeek(request.getDayOfWeek())) {
            throw new BadRequestException("Invalid day of week");
        }

        if (!ScheduleUtils.isValidPeriod(request.getStartPeriod())) {
            throw new BadRequestException("Invalid start period");
        }

        if (!ScheduleUtils.isValidTotalPeriods(request.getStartPeriod(), request.getTotalPeriod())) {
            throw new BadRequestException("Invalid total periods");
        }

        //check schedule conflict
        List<Schedule> existingSchedules = scheduleRepository.findAll();
        if (ScheduleUtils.hasScheduleConflict(
                existingSchedules,
                semesterWeek,
                request.getDayOfWeek(),
                request.getStartPeriod(),
                request.getTotalPeriod(),
                room,
                course,
                null)) {
            throw new BadRequestException("Schedule conflicts with existing schedules");
        }

        Schedule schedule = Schedule.builder()
                .course(course)
                .courseSection(courseSection)
                .room(room)
                .semesterWeek(semesterWeek)
                .dayOfWeek(request.getDayOfWeek())
                .startPeriod(request.getStartPeriod())
                .totalPeriod(request.getTotalPeriod())
                .scheduleType(request.getType())
                .scheduleStatus(ScheduleStatus.IN_PROGRESS)
                .studyDate(ScheduleUtils.generateStudyDate(semesterWeek, request.getDayOfWeek()))
                .build();

        return ScheduleMapper.mapScheduleToResponse(scheduleRepository.save(schedule));
    }

    @Override
    @Transactional
    public ScheduleResponse cancelSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        schedule.setScheduleStatus(ScheduleStatus.CANCELLED);
        return ScheduleMapper.mapScheduleToResponse(scheduleRepository.save(schedule));
    }

    //===============================================================================================================================================//

    private List<Schedule> generateSequentialPracticeSchedules(
            Course course,
            List<CourseSection> sections,
            List<Room> availableRooms,
            List<Schedule> existingSchedules,
            List<SemesterWeek> semesterWeeks,
            int totalPracticePeriods) {

        List<Schedule> createdSchedules = new ArrayList<>();

        //calculate number of lessons per section
        int sessionsPerSection = (int) Math.ceil(totalPracticePeriods / 4.0);

        //create new instance for available semester weeks
        List<SemesterWeek> availableWeeks = new ArrayList<>(semesterWeeks);

        //solve problems for each section
        for (CourseSection section : sections) {
            //find the best practice slots for the current section
            List<ScheduleSlot> bestSlots = findBestConsecutiveSlots(
                    availableRooms,
                    existingSchedules,
                    availableWeeks,
                    course,
                    sessionsPerSection);

            if (bestSlots.size() < sessionsPerSection) {
                throw new BadRequestException("Insufficient consecutive slots for section " + section.getSectionNumber());
            }

            //create schedule for the current section
            List<Schedule> sectionSchedules = new ArrayList<>();
            for (int i = 0; i < sessionsPerSection; i++) {
                ScheduleSlot slot = bestSlots.get(i);

                Schedule practiceSchedule = Schedule.builder()
                        .courseSection(section)
                        .course(course)
                        .room(slot.getRoom())
                        .semesterWeek(slot.getSemesterWeek())
                        .dayOfWeek(slot.getDayOfWeek())
                        .startPeriod(slot.getStartPeriod())
                        .totalPeriod((byte) 4)
                        .scheduleType(ScheduleType.PRACTICE)
                        .scheduleStatus(ScheduleStatus.IN_PROGRESS)
                        .studyDate(ScheduleUtils.generateStudyDate(slot.getSemesterWeek(), slot.getDayOfWeek()))
                        .build();

                Schedule savedSchedule = scheduleRepository.save(practiceSchedule);
                sectionSchedules.add(savedSchedule);

                //add to ans
                createdSchedules.add(savedSchedule);

                //add to existing schedules
                existingSchedules.add(savedSchedule);

                //remove used week from available weeks to prevent section overlap
                availableWeeks.remove(slot.getSemesterWeek());
            }
        }

        return createdSchedules;
    }

    private List<ScheduleSlot> findBestConsecutiveSlots(
            List<Room> availableRooms,
            List<Schedule> existingSchedules,
            List<SemesterWeek> availableWeeks,
            Course course,
            int requiredSessions) {

        //Group available slots by their consistency (same room, day, time)
        Map<String, List<ScheduleSlot>> slotGroups = new HashMap<>();

        //For each possible combination of room, day, and time, find available weeks
        for (Room room : availableRooms) {
            for (byte day = ScheduleUtils.MIN_DAY_OF_WEEK; day <= ScheduleUtils.MAX_DAY_OF_WEEK; day++) {
                //Try morning session
                tryTimeBlock(slotGroups, room, day, ScheduleUtils.MORNING_START,
                        availableWeeks, existingSchedules, course);

                //Try afternoon session
                tryTimeBlock(slotGroups, room, day, ScheduleUtils.AFTERNOON_START,
                        availableWeeks, existingSchedules, course);
            }
        }

        // Find the best group with most consecutive weeks
        List<ScheduleSlot> bestGroup = new ArrayList<>();

        for (List<ScheduleSlot> group : slotGroups.values()) {
            if (group.size() < requiredSessions) continue;

            // Sort by week index
            group.sort(Comparator.comparing(slot ->
                    availableWeeks.indexOf(slot.getSemesterWeek())));

            // Find the longest consecutive sequence
            List<ScheduleSlot> consecutiveSlots = findConsecutiveWeeks(group, availableWeeks);

            if (consecutiveSlots.size() >= requiredSessions &&
                    (bestGroup.isEmpty() || consecutiveSlots.size() > bestGroup.size())) {
                bestGroup = consecutiveSlots;
            }
        }

        if (bestGroup.isEmpty() || bestGroup.size() < requiredSessions) {
            throw new BadRequestException(
                    "Could not find " + requiredSessions + " consecutive weeks for scheduling");
        }

        // Return only the number of slots we need
        return bestGroup.subList(0, requiredSessions);
    }

    private void tryTimeBlock(
            Map<String, List<ScheduleSlot>> slotGroups,
            Room room,
            byte day,
            byte startPeriod,
            List<SemesterWeek> availableWeeks,
            List<Schedule> existingSchedules,
            Course course) {

        byte totalPeriod = 4;

        // Generate a key for this combination of room, day, and time
        String key = room.getId() + "-" + day + "-" + startPeriod;

        // For each available week, check if this slot is available
        for (SemesterWeek week : availableWeeks) {
            if (!ScheduleUtils.hasScheduleConflict(
                    existingSchedules, week, day, startPeriod, totalPeriod, room, course, null)) {

                // This slot is available, add it to the appropriate group
                ScheduleSlot slot = new ScheduleSlot(room, week, day, startPeriod);
                slotGroups.computeIfAbsent(key, k -> new ArrayList<>()).add(slot);
            }
        }
    }

    private List<ScheduleSlot> findConsecutiveWeeks(List<ScheduleSlot> slots, List<SemesterWeek> allWeeks) {
        if (slots.isEmpty()) return new ArrayList<>();

        // Sort by week index
        slots.sort(Comparator.comparing(slot -> allWeeks.indexOf(slot.getSemesterWeek())));

        List<ScheduleSlot> longestSequence = new ArrayList<>();
        List<ScheduleSlot> currentSequence = new ArrayList<>();
        currentSequence.add(slots.get(0));

        for (int i = 1; i < slots.size(); i++) {
            ScheduleSlot prevSlot = slots.get(i - 1);
            ScheduleSlot currSlot = slots.get(i);

            int prevWeekIndex = allWeeks.indexOf(prevSlot.getSemesterWeek());
            int currWeekIndex = allWeeks.indexOf(currSlot.getSemesterWeek());

            if (currWeekIndex == prevWeekIndex + 1) {
                // This is the next consecutive week
                currentSequence.add(currSlot);
            } else {
                // Break in sequence, check if current sequence is the longest so far
                if (currentSequence.size() > longestSequence.size()) {
                    longestSequence = new ArrayList<>(currentSequence);
                }
                // Start a new sequence
                currentSequence = new ArrayList<>();
                currentSequence.add(currSlot);
            }
        }

        // Check final sequence
        if (currentSequence.size() > longestSequence.size()) {
            longestSequence = currentSequence;
        }

        return longestSequence;
    }
}
