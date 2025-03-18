CREATE TABLE `Role` (
                        `name` varchar(36) PRIMARY KEY
);

CREATE TABLE `Permission`(
                             `name` varchar(72) PRIMARY KEY
);

CREATE TABLE `role_permission` (
                                   `role_name` VARCHAR(255),
                                   `permission_name` VARCHAR(255),
                                   PRIMARY KEY (`role_name`, `permission_name`),
                                   FOREIGN KEY (`role_name`) REFERENCES `Role`(`name`) ON DELETE CASCADE,
                                   FOREIGN KEY (`permission_name`) REFERENCES `Permission`(`name`) ON DELETE CASCADE
);

CREATE TABLE `Account` (
                           `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                           `email` varchar(50) UNIQUE NOT NULL,
                           `password` varchar(255) NOT NULL,
                           `role` varchar(36) NOT NULL,
                           `status` ENUM ('ACTIVE', 'LOCKED') NOT NULL,
                           `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `Department` (
                              `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                              `name` varchar(50) UNIQUE NOT NULL
);

CREATE TABLE `Major` (
                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                         `code` varchar(255) UNIQUE NOT NULL,
                         `name` varchar(255) UNIQUE NOT NULL,
                         `department_id` BIGINT NOT NULL
);

CREATE TABLE `Class` (
                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                         `name` varchar(100) UNIQUE NOT NULL,
                         `major_id` BIGINT NOT NULL
);

CREATE TABLE `Student_Account` (
                                   `account_id` BIGINT PRIMARY KEY,
                                   `full_name` varchar(100) NOT NULL,
                                   `code` varchar(36) UNIQUE NOT NULL,
                                   `phone` varchar(10) UNIQUE NOT NULL,
                                   `gender` bit NOT NULL,
                                   `major_id` BIGINT NOT NULL,
                                   `class_id` BIGINT NOT NULL
);

CREATE TABLE `Lecturer_Account` (
                                    `account_id` BIGINT PRIMARY KEY,
                                    `full_name` varchar(100) NOT NULL,
                                    `code` varchar(36) UNIQUE NOT NULL,
                                    `phone` varchar(10) UNIQUE NOT NULL,
                                    `gender` bit NOT NULL,
                                    `department_id` BIGINT NOT NULL
);

CREATE TABLE `Manager_Account` (
                                   `account_id` BIGINT PRIMARY KEY,
                                   `full_name` varchar(100) NOT NULL,
                                   `code` varchar(36) UNIQUE NOT NULL,
                                   `phone` varchar(10) UNIQUE NOT NULL,
                                   `gender` bit NOT NULL
);

CREATE TABLE `Semester` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `name` varchar(36) UNIQUE NOT NULL,
                            `start_date` timestamp NOT NULL,
                            `end_date` timestamp NOT NULL
);

CREATE TABLE `Subject` (
                           `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                           `code` varchar(36) UNIQUE NOT NULL,
                           `name` varchar(100) UNIQUE NOT NULL,
                           `total_credits` Int NOT NULL,
                           `total_theory_periods` BIGINT NOT NULL,
                           `total_practice_periods` BIGINT NOT NULL
);

CREATE TABLE `Course` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                          `subject_id` BIGINT NOT NULL,
                          `class_id` BIGINT NOT NULL,
                          `semester_id` BIGINT NOT NULL,
                          `lecturer_id` BIGINT NOT NULL,
                          `total_students` Int NOT NULL
);

CREATE TABLE `Course_Section` (
                                  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  `course_id` BIGINT NOT NULL,
                                  `section_number` Int UNIQUE NOT NULL,
                                  `total_students_in_section` Int NOT NULL
);

CREATE TABLE `Room` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `name` varchar(36) UNIQUE NOT NULL,
--                         `location` varchar(36) NOT NULL,
                        `capacity` INT NOT NULL,
                        `status` ENUM ('AVAILABLE', 'REPAIRING') NOT NULL,
                        `description` varchar(255) NOT NULL,
                        `last_updated` datetime NOT NULL
);

CREATE TABLE `Semester_Week` (
                                 `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 `name` varchar(100) NOT NULL,
                                 `start_date` timestamp NOT NULL,
                                 `end_date` timestamp NOT NULL,
                                 `semester_id` BIGINT NOT NULL
);

CREATE TABLE `Schedule` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `course_section_id` BIGINT NOT NULL,
                            `room_id` BIGINT NOT NULL,
                            `day_of_week` TINYINT NOT NULL CHECK (day_of_week BETWEEN 1 AND 7),
                            `start_period` Int NOT NULL,
                            `total_period` Int NOT NULL,
                            `type` ENUM ('THEORY', 'PRACTICE') NOT NULL,
                            `semester_week_id` BIGINT NOT NULL,
                            `status` ENUM ('IN_PROGRESS', 'CANCELLED') NOT NULL
);

CREATE TABLE `Schedule_Request` (
                                    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    `lecturer_id` BIGINT NOT NULL,
                                    `schedule_id` BIGINT,
                                    `new_room_id` BIGINT NOT NULL,
                                    `new_semester_week_id` BIGINT,
                                    `new_day_of_week` TINYINT NOT NULL CHECK (new_day_of_week BETWEEN 1 AND 7),
                                    `new_start_period` Int NOT NULL,
                                    `new_total_period` Int NOT NULL,
                                    `reason` varchar(255) NOT NULL,
                                    `type` ENUM ('NEW_SCHEDULE', 'CHANGE_SCHEDULE') NOT NULL,
                                    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    CHECK ((type = 'NEW_SCHEDULE' AND schedule_id IS NULL)
                                        OR
                                           (type = 'CHANGE_SCHEDULE' AND schedule_id IS NOT NULL))
);

CREATE TABLE `Schedule_Request_Log` (
                                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        `request_id` BIGINT NOT NULL,
                                        `status` ENUM ('PENDING', 'ACCEPT', 'REJECT') NOT NULL,
                                        `manager_id` BIGINT NOT NULL,
                                        `replied_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
ALTER TABLE `Schedule_Request_Log` ADD FOREIGN KEY (`manager_id`) REFERENCES `Manager_Account` (`account_id`);