package com.example.labschedulerserver.utils;

import com.example.labschedulerserver.model.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleUtils {

    // Constants
    public static final byte MIN_PERIOD = 1;
    public static final byte MAX_PERIOD = 8;
    public static final byte MORNING_START = 1;
    public static final byte MORNING_END = 4;
    public static final byte AFTERNOON_START = 5;
    public static final byte AFTERNOON_END = 8;
    public static final byte MIN_DAY_OF_WEEK = 2; // Monday
    public static final byte MAX_DAY_OF_WEEK = 7; // Saturday

    public static boolean isValidDayOfWeek(byte dayOfWeek) {
        return dayOfWeek >= MIN_DAY_OF_WEEK && dayOfWeek <= MAX_DAY_OF_WEEK;
    }

    public static boolean isValidPeriod(byte period) {
        return period >= MIN_PERIOD && period <= MAX_PERIOD;
    }

    public static boolean isValidTotalPeriods(byte startPeriod, byte totalPeriod) {
        return isValidPeriod(startPeriod) &&
                totalPeriod > 0 &&
                startPeriod + totalPeriod - 1 <= MAX_PERIOD;
    }

    public static boolean isPeriodBlockValid(byte startPeriod, byte totalPeriod) {
        if (!isValidTotalPeriods(startPeriod, totalPeriod)) {
            return false;
        }

        byte endPeriod = (byte) (startPeriod + totalPeriod - 1);

        // Check if all periods are in morning
        boolean allMorning = startPeriod >= MORNING_START && endPeriod <= MORNING_END;

        // Check if all periods are in afternoon
        boolean allAfternoon = startPeriod >= AFTERNOON_START && endPeriod <= AFTERNOON_END;

        return allMorning || allAfternoon;
    }

    public static List<ScheduleSlot> findAvailablePracticeSlots(
            List<Room> availableRooms,
            List<Schedule> existingSchedules,
            List<SemesterWeek> semesterWeeks,
            Course course) {

        List<ScheduleSlot> availableSlots = new ArrayList<>();

        // Iterate over each room, semester week, day of week, and period
        for (Room room : availableRooms) {
            for (SemesterWeek week : semesterWeeks) {
                for (byte day = MIN_DAY_OF_WEEK; day <= MAX_DAY_OF_WEEK; day++) {
                    // For practice, check morning and afternoon blocks
                    if (!hasConflictForPeriodBlock(existingSchedules, week, day, MORNING_START, (byte) 4, room, course, null)) {
                        availableSlots.add(new ScheduleSlot(room, week, day, MORNING_START));
                    }
                    if (!hasConflictForPeriodBlock(existingSchedules, week, day, AFTERNOON_START, (byte) 4, room, course, null)) {
                        availableSlots.add(new ScheduleSlot(room, week, day, AFTERNOON_START));
                    }
                }
            }
        }

        return availableSlots;
    }

    public static boolean hasScheduleConflict(
            List<Schedule> existingSchedules,
            SemesterWeek week,
            byte dayOfWeek,
            byte startPeriod,
            byte totalPeriod,
            Room room,
            Course course,
            Long excludedScheduleId) {

        return hasRoomConflict(existingSchedules, week, dayOfWeek, startPeriod, totalPeriod, room, excludedScheduleId) ||
//                hasLecturerConflict(existingSchedules, week, dayOfWeek, startPeriod, totalPeriod, course.getLecturerAccount(), excludedScheduleId) ||
                hasTheoryPracticeConflict(existingSchedules, week, dayOfWeek, startPeriod, totalPeriod, course, excludedScheduleId);
    }

    public static boolean hasConflictForPeriodBlock(
            List<Schedule> existingSchedules,
            SemesterWeek week,
            byte dayOfWeek,
            byte startPeriod,
            byte totalPeriod,
            Room room,
            Course course,
            Long excludedScheduleId) {

        if (!isPeriodBlockValid(startPeriod, totalPeriod)) {
            return true; // Invalid block is considered as conflict
        }

        return hasScheduleConflict(existingSchedules, week, dayOfWeek, startPeriod, totalPeriod, room, course, excludedScheduleId);
    }

    private static boolean hasRoomConflict(
            List<Schedule> existingSchedules,
            SemesterWeek week,
            byte dayOfWeek,
            byte startPeriod,
            byte totalPeriod,
            Room room,
            Long excludedScheduleId) {

        byte endPeriod = (byte) (startPeriod + totalPeriod - 1);

        return existingSchedules.stream()
                .filter(schedule -> !schedule.getId().equals(excludedScheduleId))
                .filter(schedule -> schedule.getRoom().getId().equals(room.getId()))
                .filter(schedule -> schedule.getSemesterWeek().getId().equals(week.getId()))
                .filter(schedule -> schedule.getDayOfWeek() == dayOfWeek)
                .anyMatch(schedule -> {
                    byte scheduleStart = schedule.getStartPeriod();
                    byte scheduleEnd = (byte) (scheduleStart + schedule.getTotalPeriod() - 1);

                    // Check for overlap
                    return (startPeriod <= scheduleEnd && endPeriod >= scheduleStart);
                });
    }

    private static boolean hasLecturerConflict(
            List<Schedule> existingSchedules,
            SemesterWeek week,
            byte dayOfWeek,
            byte startPeriod,
            byte totalPeriod,
            LecturerAccount lecturer,
            Long excludedScheduleId) {

        byte endPeriod = (byte) (startPeriod + totalPeriod - 1);

        return existingSchedules.stream()
                .filter(schedule -> !schedule.getId().equals(excludedScheduleId))
//                .filter(schedule -> schedule.getCourseSection().getCourse().getLecturerAccount().getAccountId().equals(lecturer.getAccountId()))
                .filter(schedule -> schedule.getSemesterWeek().getId().equals(week.getId()))
                .filter(schedule -> schedule.getDayOfWeek() == dayOfWeek)
                .anyMatch(schedule -> {
                    byte scheduleStart = schedule.getStartPeriod();
                    byte scheduleEnd = (byte) (scheduleStart + schedule.getTotalPeriod() - 1);

                    // Check for overlap
                    return (startPeriod <= scheduleEnd && endPeriod >= scheduleStart);
                });
    }

    private static boolean hasTheoryPracticeConflict(
            List<Schedule> existingSchedules,
            SemesterWeek week,
            byte dayOfWeek,
            byte startPeriod,
            byte totalPeriod,
            Course course,
            Long excludedScheduleId) {

        byte endPeriod = (byte) (startPeriod + totalPeriod - 1);

        return existingSchedules.stream()
                .filter(schedule -> !schedule.getId().equals(excludedScheduleId))
                .filter(schedule -> {
                    if (schedule.getCourse() != null) {
                        return schedule.getCourse().getId().equals(course.getId());
                    } else if (schedule.getCourseSection() != null) {
                        return schedule.getCourseSection().getCourse().getId().equals(course.getId());
                    }
                    return false;
                })
                .filter(schedule -> schedule.getSemesterWeek().getId().equals(week.getId()))
                .filter(schedule -> schedule.getDayOfWeek() == dayOfWeek)
                .anyMatch(schedule -> {
                    byte scheduleStart = schedule.getStartPeriod();
                    byte scheduleEnd = (byte) (scheduleStart + schedule.getTotalPeriod() - 1);

                    // Check for overlap
                    return (startPeriod <= scheduleEnd && endPeriod >= scheduleStart);
                });
    }

    public static Timestamp generateStudyDate(SemesterWeek week, byte dayOfWeek) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(week.getStartDate());

        //Adjust to the correct day of week (assuming week starts on Monday)
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);
        int targetDay = dayOfWeek;

        int daysToAdd = (targetDay - currentDay + 7) % 7;
        cal.add(Calendar.DAY_OF_WEEK, daysToAdd);

        return new Timestamp(cal.getTimeInMillis());
    }

    public static List<Room> filterRoomsByCapacity(List<Room> rooms, int requiredCapacity) {
        return rooms.stream()
                .filter(room -> room.getCapacity() >= requiredCapacity)
                .collect(Collectors.toList());
    }

//    public static ScheduleType determineScheduleType(Course course) {
//        Subject subject = course.getSubject();
//        return subject.getTotalPracticePeriods() > 0 ? ScheduleType.PRACTICE : ScheduleType.THEORY;
//    }
} 