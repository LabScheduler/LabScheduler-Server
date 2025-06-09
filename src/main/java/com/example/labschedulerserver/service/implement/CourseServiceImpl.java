package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.auth.AuthService;
import com.example.labschedulerserver.common.RoomStatus;
import com.example.labschedulerserver.common.RoomType;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.request.Course.CourseMapper;
import com.example.labschedulerserver.payload.request.Course.CreateCourseRequest;
import com.example.labschedulerserver.payload.request.Course.UpdateCourseRequest;
import com.example.labschedulerserver.payload.response.CourseResponse;
import com.example.labschedulerserver.payload.response.CourseSectionResponse;
import com.example.labschedulerserver.payload.response.NewCourseResponse;
import com.example.labschedulerserver.payload.response.Schedule.ScheduleResponse;
import com.example.labschedulerserver.payload.response.User.LecturerResponse;
import com.example.labschedulerserver.payload.response.User.UserMapper;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.CourseService;
import com.example.labschedulerserver.service.RoomService;
import com.example.labschedulerserver.service.ScheduleService;
import com.example.labschedulerserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;
    private final LecturerAccountRepository lecturerAccountRepository;
    private final CourseSectionRepository courseSectionRepository;

    private final ModelMapper modelMapper;
    private final ScheduleService scheduleService;
    private final AuthService authService;
    private final UserService userService;
    private final RoomService roomService;
    private final CourseMapper courseMapper;

    //Get all courses by the current semester
    @Override
    public List<CourseResponse> getAllCourse() {
        Semester semester = semesterRepository.findCurrentSemester().orElseThrow(() -> new ResourceNotFoundException("Semester not found"));
        return courseRepository
                .findAllBySemesterId(semester.getId())
                .stream()
                .map(courseMapper::toCourseResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> getAllCourseBySemester(Long semesterId) {
        return courseRepository.findAllBySemesterId(semesterId)
                .stream()
                .map(courseMapper::toCourseResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> getAllCourse(Long classId) {
        return courseRepository
                .findAllByClazzId(classId)
                .stream()
                .map(courseMapper::toCourseResponse)
                .toList();
    }

    @Override
    public List<CourseResponse> getAllCourse(Long subjectId, Long semesterId) {
        return courseRepository.findAllBySubjectIdAndSemesterId(subjectId, semesterId)
                .stream()
                .map(courseMapper::toCourseResponse)
                .toList();
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toCourseResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id:" + id));
    }

    @Override
    public List<CourseSectionResponse> getCourseSectionByCourseId(Long courseId) {
        List<CourseSection> courseSections = courseSectionRepository.findAllByCourseId(courseId);
        if (courseSections.isEmpty()) {
            throw new ResourceNotFoundException("CourseSection not found with courseId: " + courseId);
        }
        return courseSections.stream()
                .map(courseMapper::toCourseSectionResponse)
                .toList();
    }


    private void combine(List<Room> rooms, List<Room> current, int index, int r, List<List<Room>> result) {
        if (current.size() == r) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = index; i < rooms.size(); i++) {
            current.add(rooms.get(i));
            combine(rooms, current, i + 1, r, result);
            current.remove(current.size() - 1);
        }
    }

    //Generate the best room combination for the course
    private List<Room> generatePracticeRoomCombinations(List<Room> rooms, int maxStudentsInCourse) {
        List<Room> availableRooms = rooms.stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE && room.getType() == RoomType.COMPUTER_LAB)
                .sorted(Comparator.comparingInt(Room::getCapacity).reversed())
                .collect(Collectors.toList());

        List<List<Room>> roomCombinations = new ArrayList<>();

        for (int r = 1; r <= availableRooms.size(); r++) {
            combine(availableRooms, new ArrayList<>(), 0, r, roomCombinations);
        }

        List<Room> bestCombination = roomCombinations.stream()
                .filter(c -> c.stream().mapToInt(Room::getCapacity).sum() >= maxStudentsInCourse).min(Comparator
                        .comparingInt((List<Room> c) -> c.stream().mapToInt(Room::getCapacity).sum() - maxStudentsInCourse)
                        .thenComparing(List::size)
                        .thenComparing(c -> {
                            IntSummaryStatistics stats = c.stream().mapToInt(Room::getCapacity).summaryStatistics();
                            return stats.getMax() - stats.getMin();
                        }))
                .orElseThrow(() -> new ResourceNotFoundException("No suitable room combination found"));
        return bestCombination;
    }

    @Override
    @Transactional
    public NewCourseResponse createCourse(CreateCourseRequest request) {
        Semester semester = semesterRepository.findById(request.getSemesterId()).orElseThrow(() -> new ResourceNotFoundException("Semester not found"));
//        if(semester.getStartDate().isBefore(LocalDate.now())){
//            throw new ResourceNotFoundException("Semester has started");
//        }

        Course course = courseRepository.findCoursesBySubjectIdAndClazzIdAndSemesterId(request.getSubjectId(), request.getClassId(), semester.getId());
        if (course != null) {
            throw new ResourceNotFoundException("Course already exist");
        }
        List<Room> availableRooms = roomService.getAllRoom().stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE && room.getType() == RoomType.COMPUTER_LAB)
                .toList();
        List<Room> bestPracticeRoomCombinations = generatePracticeRoomCombinations(availableRooms, request.getTotalStudents());
        int totalSection = bestPracticeRoomCombinations.size();
        Course newCourse = Course.builder()
                .subject(subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> new ResourceNotFoundException("Subject not found")))
                .clazz(classRepository.findById(request.getClassId()).orElseThrow(() -> new ResourceNotFoundException("Class not found")))
                .lecturers(lecturerAccountRepository.findAllById(request.getLecturersIds()))
                .groupNumber(courseRepository
                        .findAllBySubjectIdAndSemesterId(request.getSubjectId(), semester.getId())
                        .stream()
                        .mapToInt(Course::getGroupNumber)
                        .max()
                        .orElse(0) + 1
                )
                .maxStudents(request.getTotalStudents())
                .semester(semester)
                .build();
        if (newCourse.getSubject().getTotalPracticePeriods() == 0) {
            throw new ResourceNotFoundException("Subject has no practice periods");
        }
        List<CourseSection> courseSections = new ArrayList<>();

        int remainingStudents = newCourse.getMaxStudents();

        for (int i = 1; i <= totalSection; i++) {
            int totalStudent = newCourse.getMaxStudents();
            int totalStudentInSection = remainingStudents - bestPracticeRoomCombinations.get(i - 1).getCapacity();

            CourseSection newCourseSection = CourseSection.builder()
                    .course(newCourse)
                    .sectionNumber(i)
                    .maxStudentsInSection(totalStudentInSection)
                    .build();
            courseSections.add(newCourseSection);
        }
        newCourse.setCourseSections(courseSections);

        courseRepository.save(newCourse);
        courseSectionRepository.saveAll(courseSections);
        return NewCourseResponse.builder()
                .course(courseMapper.toCourseResponse(newCourse))
                .schedules(generatePracticeSchedule(newCourse, request.getStartWeekId()))
                .build();
    }


    @Override
    public Course checkCourseExist(Long subjectId, Long classId, Long semesterId) {
        return courseRepository.findCoursesBySubjectIdAndClazzIdAndSemesterId(subjectId, classId, semesterId);
    }

    @Override
    public List<CourseResponse> getLecturerCourse() {
        Account account = userService.getCurrentAccount();
        LecturerAccount lecturerAccount = (LecturerAccount) userService.getAccountInfo(account);

        return courseRepository.findAllBySemesterId(semesterRepository.findCurrentSemester().orElseThrow(() -> new ResourceNotFoundException("Semester not found")).getId())
                .stream()
                .filter(course -> course.getLecturers().contains(lecturerAccount))
                .map(courseMapper::toCourseResponse)
                .toList();
    }

    @Override
    public CourseResponse updateCourse(Long id, UpdateCourseRequest request) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        course.setSubject(subjectRepository.findById(request.getSubjectId()).orElseThrow(() -> new ResourceNotFoundException("Subject not found")));
        course.setClazz(classRepository.findById(request.getClassId()).orElseThrow(() -> new ResourceNotFoundException("Class not found")));
        course.setMaxStudents(request.getTotalStudents());

        if (request.getLecturersIds() != null) {
            course.setLecturers(lecturerAccountRepository.findAllById(request.getLecturersIds()));
        }

        return courseMapper.toCourseResponse(courseRepository.save(course));
    }

    @Override
    public List<LecturerResponse> getLecturersByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        return course.getLecturers()
                .stream()
                .map(lecturerAccount -> {
                    return (LecturerResponse) UserMapper.mapUserToResponse(lecturerAccount.getAccount(), lecturerAccount);
                })
                .toList();
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        courseRepository.delete(course);
    }

    private List<ScheduleResponse> generatePracticeSchedule(Course course, Long startWeek) {
        return scheduleService.allocateSchedule(course.getId(), startWeek);
    }
}
