CREATE TABLE `role` (
                        `name` varchar(36) PRIMARY KEY
);


CREATE TABLE `account` (
                           `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                           `email` varchar(50) UNIQUE NOT NULL,
                           `password` varchar(255) NOT NULL,
                           `role` varchar(36) NOT NULL,
                           `status` ENUM ('ACTIVE', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
                           `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `department` (
                              `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                              `name` varchar(50) UNIQUE NOT NULL
);

CREATE TABLE `major` (
                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                         `code` varchar(255) UNIQUE NOT NULL,
                         `name` varchar(255) UNIQUE NOT NULL,
                         `department_id` BIGINT NOT NULL
);

CREATE TABLE `class` (
                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                         `name` varchar(100) UNIQUE NOT NULL,
                         `major_id` BIGINT NOT NULL
);

CREATE TABLE `student_account` (
                                   `account_id` BIGINT PRIMARY KEY,
                                   `full_name` varchar(100) NOT NULL,
                                   `code` varchar(36) UNIQUE NOT NULL,
                                   `phone` varchar(10) UNIQUE NOT NULL,
                                   `gender` bit NOT NULL,
                                   `major_id` BIGINT NOT NULL,
                                   `class_id` BIGINT NOT NULL
);

CREATE TABLE `lecturer_account` (
                                    `account_id` BIGINT PRIMARY KEY,
                                    `full_name` varchar(100) NOT NULL,
                                    `code` varchar(36) UNIQUE NOT NULL,
                                    `phone` varchar(10) UNIQUE NOT NULL,
                                    `gender` bit NOT NULL,
                                    `department_id` BIGINT NOT NULL
);

CREATE TABLE `manager_account` (
                                   `account_id` BIGINT PRIMARY KEY,
                                   `full_name` varchar(100) NOT NULL,
                                   `code` varchar(36) UNIQUE NOT NULL,
                                   `phone` varchar(10) UNIQUE NOT NULL,
                                   `gender` bit NOT NULL
);

CREATE TABLE `semester` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `code` varchar(36) UNIQUE NOT NULL,
                            `name` varchar(36) UNIQUE NOT NULL,
                            `start_date` timestamp NOT NULL,
                            `end_date` timestamp NOT NULL
);

CREATE TABLE `subject` (
                           `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                           `code` varchar(36) UNIQUE NOT NULL,
                           `name` varchar(100) NOT NULL,
                           `total_credits` Int NOT NULL,
                           `total_theory_periods` BIGINT NOT NULL,
                           `total_practice_periods` BIGINT NOT NULL
);

CREATE TABLE `course` (
                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                          `subject_id` BIGINT NOT NULL,
                          `class_id` BIGINT NOT NULL,
                          `semester_id` BIGINT NOT NULL,
                          `lecturer_id` BIGINT NOT NULL,
                           `group_number` Int NOT NULL,
                          `total_students` Int NOT NULL
);

CREATE TABLE `course_section` (
                                  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  `course_id` BIGINT NOT NULL,
                                  `section_number` Int NOT NULL,
                                  `total_students_in_section` Int NOT NULL,
                                  FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
);

CREATE TABLE `room` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `name` varchar(36) UNIQUE NOT NULL,
--                         `location` varchar(36) NOT NULL,
                        `capacity` INT NOT NULL,
                        `status` ENUM ('AVAILABLE','UNAVAILABLE', 'REPAIRING') NOT NULL DEFAULT 'AVAILABLE',
                        `description` varchar(255) NOT NULL,
                        `last_updated` datetime NOT NULL
);

CREATE TABLE `semester_week` (
                                 `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 `name` varchar(100) NOT NULL,
                                 `start_date` timestamp NOT NULL,
                                 `end_date` timestamp NOT NULL,
                                 `semester_id` BIGINT NOT NULL
);

CREATE TABLE `schedule` (
                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `course_id` BIGINT ,
                            `course_section_id` BIGINT,
                            `room_id` BIGINT NOT NULL,
                            `day_of_week` TINYINT NOT NULL CHECK (day_of_week BETWEEN 1 AND 7),
                            `start_period` Int NOT NULL,
                            `total_period` Int NOT NULL,
                            `type` ENUM ('THEORY', 'PRACTICE') NOT NULL,
                            `semester_week_id` BIGINT NOT NULL,
                            `status` ENUM ('IN_PROGRESS', 'CANCELLED') NOT NULL,
                            `study_date` TIMESTAMP NOT NULL,
                            FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
);

CREATE TABLE `lecturer_request` (
                                    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    `lecturer_id` BIGINT NOT NULL,
                                    `course_id` BIGINT NOT NULL,
                                    `course_section_id` BIGINT,
                                    `new_room_id` BIGINT NOT NULL,
                                    `new_semester_week_id` BIGINT,
                                    `new_day_of_week` TINYINT NOT NULL CHECK (new_day_of_week BETWEEN 1 AND 7),
                                    `new_start_period` Int NOT NULL,
                                    `new_total_period` Int NOT NULL,
                                    `reason` varchar(255) NOT NULL,
                                    `type` ENUM ('ADD_SCHEDULE') NOT NULL,
                                    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
);

CREATE TABLE `lecturer_request_log` (
                                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        `request_id` BIGINT NOT NULL,
                                        `manager_id` BIGINT,
                                        `status` ENUM ('PENDING', 'APPROVED', 'REJECT','CANCELED') NOT NULL DEFAULT 'PENDING',
                                        `replied_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



ALTER TABLE `account` ADD FOREIGN KEY (`role`) REFERENCES `role` (`name`);

ALTER TABLE `student_account` ADD FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE;
ALTER TABLE `student_account` ADD FOREIGN KEY (`major_id`) REFERENCES `major` (`id`);
ALTER TABLE `student_account` ADD FOREIGN KEY (`class_id`) REFERENCES `class` (`id`);

ALTER TABLE `lecturer_account` ADD FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE;
ALTER TABLE `lecturer_account` ADD FOREIGN KEY (`department_id`) REFERENCES `department` (`id`);

ALTER TABLE `manager_account` ADD FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE;

ALTER TABLE `major` ADD FOREIGN KEY (`department_id`) REFERENCES `department` (`id`);

ALTER TABLE `class` ADD FOREIGN KEY (`major_id`) REFERENCES `major` (`id`);

ALTER TABLE `semester_week` ADD FOREIGN KEY (`semester_id`) REFERENCES `semester` (`id`);

ALTER TABLE `course` ADD FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`);
ALTER TABLE `course` ADD FOREIGN KEY (`class_id`) REFERENCES `class` (`id`);
ALTER TABLE `course` ADD FOREIGN KEY (`semester_id`) REFERENCES `semester` (`id`);
ALTER TABLE `course` ADD FOREIGN KEY (`lecturer_id`) REFERENCES `lecturer_account` (`account_id`);

ALTER TABLE `course_section` ADD FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE;

ALTER TABLE `schedule` ADD FOREIGN KEY (`course_section_id`) REFERENCES `course_section` (`id`);
ALTER TABLE `schedule` ADD FOREIGN KEY (`room_id`) REFERENCES `room` (`id`);
ALTER TABLE `schedule` ADD FOREIGN KEY (`semester_week_id`) REFERENCES `semester_week` (`id`);

ALTER TABLE `lecturer_request` ADD FOREIGN KEY (`lecturer_id`) REFERENCES `lecturer_account` (`account_id`);
ALTER TABLE `lecturer_request` ADD FOREIGN KEY (`new_room_id`) REFERENCES `room` (`id`);
ALTER TABLE `lecturer_request` ADD FOREIGN KEY (`new_semester_week_id`) REFERENCES `semester_week` (`id`);
ALTER TABLE `lecturer_request` ADD FOREIGN KEY (`course_section_id`) REFERENCES `course_section` (`id`);

ALTER TABLE `lecturer_request_log` ADD FOREIGN KEY (`request_id`) REFERENCES `lecturer_request` (`id`);
ALTER TABLE `lecturer_request_log` ADD FOREIGN KEY (`manager_id`) REFERENCES `manager_account` (`account_id`);