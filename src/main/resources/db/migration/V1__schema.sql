-- Role Table
CREATE TABLE role (
                      name VARCHAR(36) PRIMARY KEY
);

-- Account Table
CREATE TABLE account (
                         id         BIGINT PRIMARY KEY AUTO_INCREMENT,
                         username   VARCHAR(50) UNIQUE NOT NULL,
                         password   VARCHAR(255) NOT NULL,
                         role       VARCHAR(36) NOT NULL,
                         status     ENUM('ACTIVE', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (role) REFERENCES role(name)
);

-- Department Table
CREATE TABLE department (
                            id   BIGINT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(50) UNIQUE NOT NULL
);

-- Major Table
CREATE TABLE major (
                       id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                       code          VARCHAR(255) UNIQUE NOT NULL,
                       name          VARCHAR(255) UNIQUE NOT NULL,
                       department_id BIGINT NOT NULL,
                       FOREIGN KEY (department_id) REFERENCES department(id)
);

-- Specialization Table
CREATE TABLE specialization (
                                id       BIGINT PRIMARY KEY,
                                name     VARCHAR(100) NOT NULL,
                                major_id BIGINT NOT NULL,
                                FOREIGN KEY (major_id) REFERENCES major(id)
);

-- Class Table
CREATE TABLE class (
                       id                BIGINT PRIMARY KEY,
                       name              VARCHAR(50) NOT NULL UNIQUE,
                       major_id          BIGINT NOT NULL,
                       specialization_id BIGINT,
                       class_type ENUM('MAJOR', 'SPECIALIZATION') NOT NULL,
                       FOREIGN KEY (major_id) REFERENCES major(id),
                       FOREIGN KEY (specialization_id) REFERENCES specialization(id)
);

-- Student Account Table
CREATE TABLE student_account (
                                 account_id BIGINT PRIMARY KEY,
                                 full_name  VARCHAR(100) NOT NULL,
                                 code       VARCHAR(36) UNIQUE NOT NULL,
                                 phone      VARCHAR(10) UNIQUE NOT NULL,
                                 email      VARCHAR(255) UNIQUE NOT NULL,
                                 gender     BIT NOT NULL,
                                 FOREIGN KEY (account_id) REFERENCES account(id)
);

-- Student-Class Relationship Table
CREATE TABLE student_on_class (
                                  student_id BIGINT,
                                  class_id   BIGINT,
                                  PRIMARY KEY (student_id, class_id),
                                  FOREIGN KEY (student_id) REFERENCES student_account(account_id),
                                  FOREIGN KEY (class_id) REFERENCES class(id)
);

-- Lecturer Account Table
CREATE TABLE lecturer_account (
                                  account_id    BIGINT PRIMARY KEY,
                                  full_name     VARCHAR(100) NOT NULL,
                                  code          VARCHAR(36) UNIQUE NOT NULL,
                                  email         VARCHAR(255) UNIQUE NOT NULL,
                                  phone         VARCHAR(10) UNIQUE NOT NULL,
                                  gender        BIT NOT NULL,
                                  department_id BIGINT NOT NULL,
                                  FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
                                  FOREIGN KEY (department_id) REFERENCES department(id)
);

-- Manager Account Table
CREATE TABLE manager_account (
                                 account_id BIGINT PRIMARY KEY,
                                 full_name  VARCHAR(100) NOT NULL,
                                 code       VARCHAR(36) UNIQUE NOT NULL,
                                 email      VARCHAR(255) UNIQUE NOT NULL,
                                 phone      VARCHAR(10) UNIQUE NOT NULL,
                                 gender     BIT NOT NULL,
                                 FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
);

-- Semester Table
CREATE TABLE semester (
                          id         BIGINT PRIMARY KEY AUTO_INCREMENT,
                          code       VARCHAR(36) UNIQUE NOT NULL,
                          name       VARCHAR(36) UNIQUE NOT NULL,
                          start_date TIMESTAMP NOT NULL,
                          end_date   TIMESTAMP NOT NULL
);

-- Subject Table
CREATE TABLE subject (
                         id                     BIGINT PRIMARY KEY AUTO_INCREMENT,
                         code                   VARCHAR(36) UNIQUE NOT NULL,
                         name                   VARCHAR(100) NOT NULL,
                         total_credits          INT NOT NULL,
                         total_theory_periods   INT NOT NULL,
                         total_practice_periods INT NOT NULL,
                         total_exercise_periods INT NOT NULL,
                         total_self_study_periods INT NOT NULL
);

-- Course Table
CREATE TABLE course (
                        id             BIGINT PRIMARY KEY AUTO_INCREMENT,
                        subject_id     BIGINT NOT NULL,
                        class_id       BIGINT NOT NULL,
                        semester_id    BIGINT NOT NULL,
                        group_number   INT NOT NULL,
                        FOREIGN KEY (subject_id) REFERENCES subject(id),
                        FOREIGN KEY (class_id) REFERENCES class(id),
                        FOREIGN KEY (semester_id) REFERENCES semester(id)
);

-- Lecturer-Course Relationship Table
CREATE TABLE lecturer_on_course (
                                    course_id   BIGINT,
                                    lecturer_id BIGINT,
                                    PRIMARY KEY (course_id, lecturer_id),
                                    FOREIGN KEY (course_id) REFERENCES course(id),
                                    FOREIGN KEY (lecturer_id) REFERENCES lecturer_account(account_id)
);

-- Course Section Table
CREATE TABLE course_section (
                                id                        BIGINT PRIMARY KEY AUTO_INCREMENT,
                                course_id                 BIGINT NOT NULL,
                                section_number            INT NOT NULL,
                                total_students_in_section INT NOT NULL,
                                FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);

-- Room Table
CREATE TABLE room (
                      id            BIGINT PRIMARY KEY AUTO_INCREMENT,
                      name          VARCHAR(36) UNIQUE NOT NULL,
                      capacity      INT NOT NULL,
                      status        ENUM('AVAILABLE', 'UNAVAILABLE', 'REPAIRING') NOT NULL DEFAULT 'AVAILABLE',
                      description   VARCHAR(255) NOT NULL,
                      last_updated  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Semester Week Table
CREATE TABLE semester_week (
                               id          BIGINT PRIMARY KEY AUTO_INCREMENT,
                               name        VARCHAR(100) NOT NULL,
                               start_date  TIMESTAMP NOT NULL,
                               end_date    TIMESTAMP NOT NULL,
                               semester_id BIGINT NOT NULL,
                               FOREIGN KEY (semester_id) REFERENCES semester(id)
);

-- Schedule Table
CREATE TABLE schedule (
                          id                BIGINT PRIMARY KEY AUTO_INCREMENT,
                          course_id         BIGINT,
                          course_section_id BIGINT,
                          room_id           BIGINT NOT NULL,
                          day_of_week       TINYINT NOT NULL CHECK (day_of_week BETWEEN 2 AND 7),
                          lecturer_id       BIGINT NOT NULL,
                          start_period      INT NOT NULL,
                          total_period      INT NOT NULL,
                          semester_week_id  BIGINT NOT NULL,
                          status            ENUM('IN_PROGRESS','COMPLETED', 'CANCELLED') NOT NULL,
                          study_date        TIMESTAMP NOT NULL,
                          FOREIGN KEY (course_id) REFERENCES course(id),
                          FOREIGN KEY (course_section_id) REFERENCES course_section(id),
                          FOREIGN KEY (room_id) REFERENCES room(id),
                          FOREIGN KEY (lecturer_id) REFERENCES lecturer_account(account_id),
                          FOREIGN KEY (semester_week_id) REFERENCES semester_week(id)
);

-- Lecturer Request Table
CREATE TABLE lecturer_request (
                                  id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  lecturer_id          BIGINT NOT NULL,
                                  course_id            BIGINT NOT NULL,
                                  course_section_id    BIGINT,
                                  room_id          BIGINT NOT NULL,
                                  semester_week_id BIGINT,
                                  day_of_week      TINYINT NOT NULL CHECK (day_of_week BETWEEN 2 AND 7),
                                  start_period     INT NOT NULL,
                                  total_period     INT NOT NULL,
                                  reason               VARCHAR(255) NOT NULL,
                                  created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  FOREIGN KEY (lecturer_id) REFERENCES lecturer_account(account_id),
                                  FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
                                  FOREIGN KEY (course_section_id) REFERENCES course_section(id),
                                  FOREIGN KEY (room_id) REFERENCES room(id),
                                  FOREIGN KEY (semester_week_id) REFERENCES semester_week(id)
);

-- Lecturer Request Log Table
CREATE TABLE lecturer_request_log (
                                      id         BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      request_id BIGINT NOT NULL,
                                      manager_id BIGINT,
                                      status     ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
                                      body VARCHAR(255),
                                      replied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      FOREIGN KEY (request_id) REFERENCES lecturer_request(id),
                                      FOREIGN KEY (manager_id) REFERENCES manager_account(account_id)
);