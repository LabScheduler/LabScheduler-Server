CREATE TABLE `Role` (
                        `name` varchar(255) PRIMARY KEY
);

CREATE TABLE `Account` (
                           `id` Integer PRIMARY KEY AUTO_INCREMENT,
                           `email` varchar(255) UNIQUE NOT NULL,
                           `password` varchar(255) NOT NULL,
                           `role` varchar(255) NOT NULL,
                           `status` ENUM ('ACTIVE', 'LOCKED') NOT NULL
);

CREATE TABLE `Department` (
                              `id` Integer PRIMARY KEY AUTO_INCREMENT,
                              `name` varchar(255) UNIQUE NOT NULL
);

CREATE TABLE `Major` (
                         `id` Integer PRIMARY KEY AUTO_INCREMENT,
                         `code` varchar(255) NOT NULL,
                         `name` varchar(255) NOT NULL,
                         `department_id` Integer NOT NULL
);

CREATE TABLE `Class` (
                         `id` Integer PRIMARY KEY AUTO_INCREMENT,
                         `name` varchar(255) UNIQUE NOT NULL,
                         `major_id` Integer NOT NULL
);

CREATE TABLE `Student_Account` (
                                   `account_id` Integer PRIMARY KEY,
                                   `full_name` varchar(255) NOT NULL,
                                   `code` varchar(255) NOT NULL,
                                   `phone` varchar(255) UNIQUE NOT NULL,
                                   `gender` bit NOT NULL,
                                   `major_id` Integer NOT NULL,
                                   `class_id` Integer NOT NULL
);

CREATE TABLE `Lecturer_Account` (
                                    `account_id` Integer PRIMARY KEY,
                                    `code` varchar(255) NOT NULL,
                                    `full_name` varchar(255) NOT NULL,
                                    `department_id` Integer NOT NULL,
                                    `phone` varchar(255) UNIQUE NOT NULL,
                                    `gender` bit NOT NULL
);

CREATE TABLE `Manager_Account` (
                                   `account_id` Integer PRIMARY KEY,
                                   `full_name` varchar(255) NOT NULL,
                                   `code` varchar(255) NOT NULL,
                                   `phone` varchar(255) UNIQUE NOT NULL,
                                   `gender` bit NOT NULL
);

CREATE TABLE `Semester` (
                            `id` Integer PRIMARY KEY AUTO_INCREMENT,
                            `name` varchar(255) UNIQUE NOT NULL,
                            `start_date` timestamp NOT NULL,
                            `end_date` timestamp NOT NULL
);

CREATE TABLE `Subject` (
                           `id` Integer PRIMARY KEY AUTO_INCREMENT,
                           `code` varchar(255) UNIQUE NOT NULL,
                           `name` varchar(255) NOT NULL,
                           `total_credits` Int NOT NULL,
                           `total_on_class_periods` Int NOT NULL,
                           `total_practice_periods` Int NOT NULL
);

CREATE TABLE `Course` (
                          `id` Integer PRIMARY KEY AUTO_INCREMENT,
                          `subject_id` Integer NOT NULL,
                          `class_id` Integer NOT NULL,
                          `semester_id` Integer NOT NULL,
                          `lecturer_id` Integer NOT NULL,
                          `total_students` Int NOT NULL
);

-- Bảng Course_Section (tham chiếu Course)
CREATE TABLE `Course_Section` (
                                  `id` Integer PRIMARY KEY AUTO_INCREMENT,
                                  `course_id` Integer NOT NULL,
                                  `section_number` Int NOT NULL,
                                  `total_students_in_section` Int NOT NULL
);

CREATE TABLE `Room` (
                        `id` Integer PRIMARY KEY AUTO_INCREMENT,
                        `name` varchar(255) UNIQUE NOT NULL,
                        `location` varchar(255) NOT NULL,
                        `capacity` integer NOT NULL,
                        `status` ENUM ('AVAILABLE', 'UNAVAILABLE', 'REPAIRING') NOT NULL,
                        `description` varchar(255) NOT NULL,
                        `last_updated` datetime NOT NULL
);

CREATE TABLE `Semester_Week` (
                                 `id` Integer PRIMARY KEY AUTO_INCREMENT,
                                 `name` varchar(255) NOT NULL,
                                 `start_date` timestamp NOT NULL,
                                 `end_date` timestamp NOT NULL,
                                 `semester_id` Integer NOT NULL
);

CREATE TABLE `Schedule` (
                            `id` Integer PRIMARY KEY AUTO_INCREMENT,
                            `course_section_id` Integer NOT NULL,
                            `room_id` Integer NOT NULL,
                            `day_of_week` varchar(255) NOT NULL,
                            `start_period` Int NOT NULL,
                            `total_period` Int NOT NULL,
                            `type` ENUM ('THEORY', 'PRACTICE') NOT NULL,
                            `semester_week_id` Integer NOT NULL,
                            `status` ENUM ('IN_PROGRESS', 'CANCELLED') NOT NULL
);

CREATE TABLE `Schedule_Request` (
                                    `id` Integer PRIMARY KEY AUTO_INCREMENT,
                                    `lecturer_id` Integer NOT NULL,
                                    `schedule_id` Integer,
                                    `new_room_id` Integer NOT NULL,
                                    `new_semester_week_id` Integer,
                                    `new_day_of_week` varchar(255),
                                    `new_start_period` Int,
                                    `new_total_period` Int,
                                    `reason` varchar(255) NOT NULL,
                                    `type` ENUM ('NEW_SCHEDULE', 'CHANGE_SCHEDULE') NOT NULL,
                                    `created_at` timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE `Schedule_Request_Log` (
                                        `id` Integer PRIMARY KEY AUTO_INCREMENT,
                                        `request_id` Integer NOT NULL,
                                        `status` ENUM ('PENDING', 'ACCEPT', 'REJECT') NOT NULL,
                                        `Manager_Account_id` Integer NOT NULL,
                                        `updated_at` timestamp NOT NULL DEFAULT (now())
);

CREATE TABLE `Permission`(
                             `name` varchar(255) PRIMARY KEY
);

CREATE TABLE `role_permission` (
                                   `role_name` VARCHAR(255),
                                   `permission_name` VARCHAR(255),
                                   PRIMARY KEY (`role_name`, `permission_name`),
                                   FOREIGN KEY (`role_name`) REFERENCES `Role`(`name`) ON DELETE CASCADE,
                                   FOREIGN KEY (`permission_name`) REFERENCES `Permission`(`name`) ON DELETE CASCADE
);

ALTER TABLE `Account` ADD FOREIGN KEY (`role`) REFERENCES `Role` (`name`);

ALTER TABLE `Student_Account` ADD FOREIGN KEY (`account_id`) REFERENCES `Account` (`id`);
ALTER TABLE `Student_Account` ADD FOREIGN KEY (`major_id`) REFERENCES `Major` (`id`);
ALTER TABLE `Student_Account` ADD FOREIGN KEY (`class_id`) REFERENCES `Class` (`id`);

ALTER TABLE `Lecturer_Account` ADD FOREIGN KEY (`account_id`) REFERENCES `Account` (`id`);
ALTER TABLE `Lecturer_Account` ADD FOREIGN KEY (`department_id`) REFERENCES `Department` (`id`);

ALTER TABLE `Manager_Account` ADD FOREIGN KEY (`account_id`) REFERENCES `Account` (`id`);

ALTER TABLE `Major` ADD FOREIGN KEY (`department_id`) REFERENCES `Department` (`id`);

ALTER TABLE `Class` ADD FOREIGN KEY (`major_id`) REFERENCES `Major` (`id`);

ALTER TABLE `Semester_Week` ADD FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);

ALTER TABLE `Course` ADD FOREIGN KEY (`subject_id`) REFERENCES `Subject` (`id`);
ALTER TABLE `Course` ADD FOREIGN KEY (`class_id`) REFERENCES `Class` (`id`);
ALTER TABLE `Course` ADD FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `Course` ADD FOREIGN KEY (`lecturer_id`) REFERENCES `Lecturer_Account` (`account_id`);

ALTER TABLE `Course_Section` ADD FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);

ALTER TABLE `Schedule` ADD FOREIGN KEY (`course_section_id`) REFERENCES `Course_Section` (`id`);
ALTER TABLE `Schedule` ADD FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `Schedule` ADD FOREIGN KEY (`semester_week_id`) REFERENCES `Semester_Week` (`id`);

ALTER TABLE `Schedule_Request` ADD FOREIGN KEY (`schedule_id`) REFERENCES `Schedule` (`id`);
ALTER TABLE `Schedule_Request` ADD FOREIGN KEY (`lecturer_id`) REFERENCES `Lecturer_Account` (`account_id`);
ALTER TABLE `Schedule_Request` ADD FOREIGN KEY (`new_room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `Schedule_Request` ADD FOREIGN KEY (`new_semester_week_id`) REFERENCES `Semester_Week` (`id`);

ALTER TABLE `Schedule_Request_Log` ADD FOREIGN KEY (`request_id`) REFERENCES `Schedule_Request` (`id`);
ALTER TABLE `Schedule_Request_Log` ADD FOREIGN KEY (`Manager_Account_id`) REFERENCES `Manager_Account` (`account_id`);