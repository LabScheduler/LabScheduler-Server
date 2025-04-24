-- Role Table
CREATE TABLE role
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(36) UNIQUE
);

-- Account Table
CREATE TABLE account
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    username   VARCHAR(50) UNIQUE        NOT NULL,
    password   VARCHAR(255)              NOT NULL,
    role_id    BIGINT                    NOT NULL,
    status     ENUM ('ACTIVE', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP                          DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES role (id)
);

-- Department Table
CREATE TABLE department
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Major Table
CREATE TABLE major
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    code          VARCHAR(255) UNIQUE NOT NULL,
    name          VARCHAR(255) UNIQUE NOT NULL,
    department_id BIGINT              NOT NULL,
    FOREIGN KEY (department_id) REFERENCES department (id)
);

-- Specialization Table
CREATE TABLE specialization
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    major_id BIGINT       NOT NULL,
    FOREIGN KEY (major_id) REFERENCES major (id)
);

-- Class Table
CREATE TABLE class
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    name              VARCHAR(50)                      NOT NULL UNIQUE,
    major_id          BIGINT                           NOT NULL,
    specialization_id BIGINT,
    class_type        ENUM ('MAJOR', 'SPECIALIZATION') NOT NULL,
    FOREIGN KEY (major_id) REFERENCES major (id),
    FOREIGN KEY (specialization_id) REFERENCES specialization (id)
);

-- Student Account Table
CREATE TABLE student_account
(
    account_id BIGINT PRIMARY KEY,
    full_name  VARCHAR(100)        NOT NULL,
    code       VARCHAR(36) UNIQUE  NOT NULL,
    phone      VARCHAR(10) UNIQUE  NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    gender     BIT                 NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account (id)
);

-- Student-Class Relationship Table
CREATE TABLE student_on_class
(
    student_id BIGINT,
    class_id   BIGINT,
    status    ENUM ('ENROLLED', 'COMPLETED', 'DROPPED') NOT NULL DEFAULT 'ENROLLED',
    PRIMARY KEY (student_id, class_id),
    FOREIGN KEY (student_id) REFERENCES student_account (account_id),
    FOREIGN KEY (class_id) REFERENCES class (id)
);

-- Lecturer Account Table
CREATE TABLE lecturer_account
(
    account_id    BIGINT PRIMARY KEY,
    full_name     VARCHAR(100)        NOT NULL,
    code          VARCHAR(36) UNIQUE  NOT NULL,
    email         VARCHAR(255) UNIQUE NOT NULL,
    phone         VARCHAR(10) UNIQUE  NOT NULL,
    gender        BIT                 NOT NULL,
    department_id BIGINT              NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES department (id)
);

-- Manager Account Table
CREATE TABLE manager_account
(
    account_id BIGINT PRIMARY KEY,
    full_name  VARCHAR(100)        NOT NULL,
    code       VARCHAR(36) UNIQUE  NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    phone      VARCHAR(10) UNIQUE  NOT NULL,
    gender     BIT                 NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
);

-- Semester Table
CREATE TABLE semester
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    code       VARCHAR(36) UNIQUE NOT NULL,
    name       VARCHAR(36) UNIQUE NOT NULL,
    start_date TIMESTAMP          NOT NULL,
    end_date   TIMESTAMP          NOT NULL
);

-- Subject Table
CREATE TABLE subject
(
    id                       BIGINT PRIMARY KEY AUTO_INCREMENT,
    code                     VARCHAR(36) UNIQUE NOT NULL,
    name                     VARCHAR(100)       NOT NULL,
    total_credits            INT                NOT NULL,
    total_theory_periods     INT                NOT NULL,
    total_practice_periods   INT                NOT NULL,
    total_exercise_periods   INT                NOT NULL,
    total_self_study_periods INT                NOT NULL
);

-- Course Table
CREATE TABLE course
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    subject_id   BIGINT NOT NULL,
    class_id     BIGINT NOT NULL,
    semester_id  BIGINT NOT NULL,
    group_number INT    NOT NULL,
    total_students INT    NOT NULL,
    FOREIGN KEY (subject_id) REFERENCES subject (id),
    FOREIGN KEY (class_id) REFERENCES class (id),
    FOREIGN KEY (semester_id) REFERENCES semester (id)
);

-- Lecturer-Course Relationship Table
CREATE TABLE lecturer_on_course
(
    course_id   BIGINT,
    lecturer_id BIGINT,
    PRIMARY KEY (course_id, lecturer_id),
    FOREIGN KEY (course_id) REFERENCES course (id),
    FOREIGN KEY (lecturer_id) REFERENCES lecturer_account (account_id)
);

-- Course Section Table
CREATE TABLE course_section
(
    id                        BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id                 BIGINT NOT NULL,
    section_number            INT    NOT NULL,
    total_students_in_section INT    NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE CASCADE
);

-- Room Table
CREATE TABLE room
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(36) UNIQUE                             NOT NULL,
    capacity     INT                                            NOT NULL,
    status       ENUM ('AVAILABLE', 'UNAVAILABLE', 'REPAIRING') NOT NULL DEFAULT 'AVAILABLE',
    description  VARCHAR(255)                                   NOT NULL,
    last_updated DATETIME                                       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Semester Week Table
CREATE TABLE semester_week
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    start_date  TIMESTAMP    NOT NULL,
    end_date    TIMESTAMP    NOT NULL,
    semester_id BIGINT       NOT NULL,
    FOREIGN KEY (semester_id) REFERENCES semester (id)
);

-- Schedule Table
CREATE TABLE schedule
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id         BIGINT,
    course_section_id BIGINT,
    room_id           BIGINT                                        NOT NULL,
    day_of_week       TINYINT                                       NOT NULL CHECK (day_of_week BETWEEN 2 AND 7),
    lecturer_id       BIGINT                                        NOT NULL,
    start_period      INT                                           NOT NULL,
    total_period      INT                                           NOT NULL,
    semester_week_id  BIGINT                                        NOT NULL,
    status            ENUM ('IN_PROGRESS','COMPLETED', 'CANCELLED') NOT NULL,
    study_date        TIMESTAMP                                     NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course (id),
    FOREIGN KEY (course_section_id) REFERENCES course_section (id),
    FOREIGN KEY (room_id) REFERENCES room (id),
    FOREIGN KEY (lecturer_id) REFERENCES lecturer_account (account_id),
    FOREIGN KEY (semester_week_id) REFERENCES semester_week (id)
);

-- Lecturer Request Table
CREATE TABLE lecturer_request
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    lecturer_id       BIGINT       NOT NULL,
    course_id         BIGINT       NOT NULL,
    course_section_id BIGINT,
    room_id           BIGINT       NOT NULL,
    semester_week_id  BIGINT,
    day_of_week       TINYINT      NOT NULL CHECK (day_of_week BETWEEN 2 AND 7),
    start_period      INT          NOT NULL,
    total_period      INT          NOT NULL,
    reason            VARCHAR(255) NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lecturer_id) REFERENCES lecturer_account (account_id),
    FOREIGN KEY (course_id) REFERENCES course (id) ON DELETE CASCADE,
    FOREIGN KEY (course_section_id) REFERENCES course_section (id),
    FOREIGN KEY (room_id) REFERENCES room (id),
    FOREIGN KEY (semester_week_id) REFERENCES semester_week (id)
);

-- Lecturer Request Log Table
CREATE TABLE lecturer_request_log
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    request_id BIGINT                                                NOT NULL,
    manager_id BIGINT,
    status     ENUM ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    body       VARCHAR(255),
    replied_at TIMESTAMP                                                      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES lecturer_request (id),
    FOREIGN KEY (manager_id) REFERENCES manager_account (account_id)
);

-- INDEX
#
# -- account
# CREATE INDEX idx_account_role ON account(role);
#
# -- major
# CREATE INDEX idx_major_department_id ON major(department_id);
#
# -- specialization
# CREATE INDEX idx_specialization_major_id ON specialization(major_id);
#
# -- class
# CREATE INDEX idx_class_major_id ON class(major_id);
# CREATE INDEX idx_class_specialization_id ON class(specialization_id);
#
# -- student_on_class
# CREATE INDEX idx_student_on_class_student_id ON student_on_class(student_id);
# CREATE INDEX idx_student_on_class_class_id ON student_on_class(class_id);
#
# -- lecturer_account
# CREATE INDEX idx_lecturer_department_id ON lecturer_account(department_id);
#
# -- course
# CREATE INDEX idx_course_subject_id ON course(subject_id);
# CREATE INDEX idx_course_class_id ON course(class_id);
# CREATE INDEX idx_course_semester_id ON course(semester_id);
#
# -- lecturer_on_course
# CREATE INDEX idx_lecturer_on_course_lecturer_id ON lecturer_on_course(lecturer_id);
#
# -- course_section
# CREATE INDEX idx_course_section_course_id ON course_section(course_id);
#
# -- semester_week
# CREATE INDEX idx_semester_week_semester_id ON semester_week(semester_id);
#
# -- schedule
# CREATE INDEX idx_schedule_course_id ON schedule(course_id);
# CREATE INDEX idx_schedule_course_section_id ON schedule(course_section_id);
# CREATE INDEX idx_schedule_room_id ON schedule(room_id);
# CREATE INDEX idx_schedule_lecturer_id ON schedule(lecturer_id);
# CREATE INDEX idx_schedule_semester_week_id ON schedule(semester_week_id);
# CREATE INDEX idx_schedule_day_of_week ON schedule(day_of_week);
# CREATE INDEX idx_schedule_study_date ON schedule(study_date);
#
# -- lecturer_request
# CREATE INDEX idx_lecturer_request_lecturer_id ON lecturer_request(lecturer_id);
# CREATE INDEX idx_lecturer_request_course_id ON lecturer_request(course_id);
# CREATE INDEX idx_lecturer_request_course_section_id ON lecturer_request(course_section_id);
# CREATE INDEX idx_lecturer_request_room_id ON lecturer_request(room_id);
# CREATE INDEX idx_lecturer_request_semester_week_id ON lecturer_request(semester_week_id);
# CREATE INDEX idx_lecturer_request_day_of_week ON lecturer_request(day_of_week);
#
# -- lecturer_request_log
# CREATE INDEX idx_request_log_request_id ON lecturer_request_log(request_id);
# CREATE INDEX idx_request_log_manager_id ON lecturer_request_log(manager_id);