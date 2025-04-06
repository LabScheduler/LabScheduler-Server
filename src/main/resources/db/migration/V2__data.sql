INSERT INTO role (name)
    VALUE
    ('MANAGER'),
    ('LECTURER'),
    ('STUDENT');

INSERT INTO account (id, email, password, role, status) #pass =123
VALUES (1, 'gv1@lecturer.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'LECTURER',
        'ACTIVE'),
       (2, 'gv2@lecturer.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'LECTURER',
        'ACTIVE'),
       (3, 'gv3@lecturer.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'LECTURER',
        'ACTIVE'),
       (4, 'gv4@lecturer.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'LECTURER',
        'ACTIVE'),
       (5, 'gv5@lecturer.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'LECTURER',
        'ACTIVE'),
       (6, 'manager1@manager.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'MANAGER',
        'ACTIVE'),
       (7, 'n22dccn001@student.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO',
        'STUDENT', 'ACTIVE');



INSERT INTO department (id, name)
VALUES (1, 'Khoa Công nghệ Thông tin');
INSERT INTO department (id, name)
VALUES (2, 'Khoa Viễn thông');
INSERT INTO department (id, name)
VALUES (3, 'Khoa Điện tử');
INSERT INTO department (id, name)
VALUES (4, 'Khoa Kinh tế');
INSERT INTO department (id, name)
VALUES (5, 'Khoa Đa phương tiện');

INSERT INTO major (id, code, name, department_id)
VALUES (1, 'CNTT', 'Công nghệ thông tin', 1);
INSERT INTO major (id, code, name, department_id)
VALUES (2, 'KTMT', 'Kỹ thuật máy tính', 1);
INSERT INTO major (id, code, name, department_id)
VALUES (3, 'KTVT', 'Kỹ thuật Viễn thông', 2);
INSERT INTO major (id, code, name, department_id)
VALUES (4, 'TTMMT', 'Truyền thông và Mạng máy tính', 2);
INSERT INTO major (id, code, name, department_id)
VALUES (5, 'KTDT', 'Kỹ thuật Điện tử', 3);
INSERT INTO major (id, code, name, department_id)
VALUES (6, 'KTDTVT', 'Kỹ thuật Điện tử - Viễn thông', 3);
INSERT INTO major (id, code, name, department_id)
VALUES (7, 'QTKD', 'Quản trị Kinh doanh', 4);
INSERT INTO major (id, code, name, department_id)
VALUES (8, 'KT', 'Kế toán', 4);
INSERT INTO major (id, code, name, department_id)
VALUES (9, 'TTDPT', 'Truyền thông Đa phương tiện', 5);
INSERT INTO major (id, code, name, department_id)
VALUES (10, 'CNDPT', 'Công nghệ Đa phương tiện', 5);

INSERT INTO class (name, major_id)
VALUES ('D22CQCN01-N', 1);
INSERT INTO class (name, major_id)
VALUES ('D22CQCN02-N', 1);
INSERT INTO class (name, major_id)
VALUES ('D22CQPT01-N', 8);
INSERT INTO class (name, major_id)
VALUES ('D22CQDT01-N', 5);

INSERT INTO student_account (account_id, full_name, code, phone, gender, major_id, class_id)
VALUES (7, 'Nguyen Van A', 'n22dccn001', '0123456789', 1, 1, 1);

INSERT INTO manager_account (account_id, full_name, code, phone, gender)
VALUES (6, 'Son Dinh', 'manager001', '0123123123', 1);

INSERT INTO lecturer_account (account_id, code, full_name, department_id, phone, gender)
VALUES (1, 'lecturer001', 'Huynh Trong Thua', 1, '011111111', 1),
       (2, 'lecturer002', 'Luu Nguyen Ky Thu', 1, '022222222', 1),
       (3, 'lecturer003', 'Ha Thanh', 1, '0333333333', 1),
       (4, 'lecturer004', 'Nguyen Hai', 1, '0444444444', 1),
       (5, 'lecturer005', 'Nguyen Van Nhat', 1, '0555555555', 1);



INSERT INTO semester (id, code, name, start_date, end_date)
VALUES (1, '2024-1', 'Học kỳ 1 - Năm học 2024 - 2025', '2024-8-12', '2024-12-29'),
       (2, '2024-2', 'Học kỳ 2 - Năm học 2024 - 2025', '2024-12-30', '2025-05-26'),
       (3, '2024-3', 'Học kỳ 3 - Năm học 2024 - 2025', '2025-06-23', '2025-10-13');

-- Học kỳ 1: 12/08/2024 - 29/12/2024 (20 tuần)
INSERT INTO semester_week (id, name, start_date, end_date, semester_id)
VALUES (1, 'Tuần 1', '2024-08-12 00:00:00', '2024-08-18 23:59:59', 1),
       (2, 'Tuần 2', '2024-08-19 00:00:00', '2024-08-25 23:59:59', 1),
       (3, 'Tuần 3', '2024-08-26 00:00:00', '2024-09-01 23:59:59', 1),
       (4, 'Tuần 4', '2024-09-02 00:00:00', '2024-09-08 23:59:59', 1),
       (5, 'Tuần 5', '2024-09-09 00:00:00', '2024-09-15 23:59:59', 1),
       (6, 'Tuần 6', '2024-09-16 00:00:00', '2024-09-22 23:59:59', 1),
       (7, 'Tuần 7', '2024-09-23 00:00:00', '2024-09-29 23:59:59', 1),
       (8, 'Tuần 8', '2024-09-30 00:00:00', '2024-10-06 23:59:59', 1),
       (9, 'Tuần 9', '2024-10-07 00:00:00', '2024-10-13 23:59:59', 1),
       (10, 'Tuần 10', '2024-10-14 00:00:00', '2024-10-20 23:59:59', 1),
       (11, 'Tuần 11', '2024-10-21 00:00:00', '2024-10-27 23:59:59', 1),
       (12, 'Tuần 12', '2024-10-28 00:00:00', '2024-11-03 23:59:59', 1),
       (13, 'Tuần 13', '2024-11-04 00:00:00', '2024-11-10 23:59:59', 1),
       (14, 'Tuần 14', '2024-11-11 00:00:00', '2024-11-17 23:59:59', 1),
       (15, 'Tuần 15', '2024-11-18 00:00:00', '2024-11-24 23:59:59', 1),
       (16, 'Tuần 16', '2024-11-25 00:00:00', '2024-12-01 23:59:59', 1),
       (17, 'Tuần 17', '2024-12-02 00:00:00', '2024-12-08 23:59:59', 1),
       (18, 'Tuần 18', '2024-12-09 00:00:00', '2024-12-15 23:59:59', 1),
       (19, 'Tuần 19', '2024-12-16 00:00:00', '2024-12-22 23:59:59', 1),
       (20, 'Tuần 20', '2024-12-23 00:00:00', '2024-12-29 23:59:59', 1);

-- Học kỳ 2: 30/12/2024 - 26/05/2025 (21 tuần)
INSERT INTO semester_week (id, name, start_date, end_date, semester_id)
VALUES (21, 'Tuần 21', '2024-12-30 00:00:00', '2025-01-05 23:59:59', 2),
       (22, 'Tuần 22', '2025-01-06 00:00:00', '2025-01-12 23:59:59', 2),
       (23, 'Tuần 23', '2025-01-13 00:00:00', '2025-01-19 23:59:59', 2),
       (24, 'Tuần 24', '2025-01-20 00:00:00', '2025-01-26 23:59:59', 2),
       (25, 'Tuần 25', '2025-01-27 00:00:00', '2025-02-02 23:59:59', 2),
       (26, 'Tuần 26', '2025-02-03 00:00:00', '2025-02-09 23:59:59', 2),
       (27, 'Tuần 27', '2025-02-10 00:00:00', '2025-02-16 23:59:59', 2),
       (28, 'Tuần 28', '2025-02-17 00:00:00', '2025-02-23 23:59:59', 2),
       (29, 'Tuần 29', '2025-02-24 00:00:00', '2025-03-02 23:59:59', 2),
       (30, 'Tuần 30', '2025-03-03 00:00:00', '2025-03-09 23:59:59', 2),
       (31, 'Tuần 31', '2025-03-10 00:00:00', '2025-03-16 23:59:59', 2),
       (32, 'Tuần 32', '2025-03-17 00:00:00', '2025-03-23 23:59:59', 2),
       (33, 'Tuần 33', '2025-03-24 00:00:00', '2025-03-30 23:59:59', 2),
       (34, 'Tuần 34', '2025-03-31 00:00:00', '2025-04-06 23:59:59', 2),
       (35, 'Tuần 35', '2025-04-07 00:00:00', '2025-04-13 23:59:59', 2),
       (36, 'Tuần 36', '2025-04-14 00:00:00', '2025-04-20 23:59:59', 2),
       (37, 'Tuần 37', '2025-04-21 00:00:00', '2025-04-27 23:59:59', 2),
       (38, 'Tuần 38', '2025-04-28 00:00:00', '2025-05-04 23:59:59', 2),
       (39, 'Tuần 39', '2025-05-05 00:00:00', '2025-05-11 23:59:59', 2),
       (40, 'Tuần 40', '2025-05-12 00:00:00', '2025-05-18 23:59:59', 2),
       (41, 'Tuần 41', '2025-05-19 00:00:00', '2025-05-25 23:59:59', 2);

-- Học kỳ 3: 23/06/2025 - 13/10/2025 (16 tuần)
INSERT INTO semester_week (id, name, start_date, end_date, semester_id)
VALUES (42, 'Tuần 46', '2025-06-23 00:00:00', '2025-06-29 23:59:59', 3),
       (43, 'Tuần 47', '2025-06-30 00:00:00', '2025-07-06 23:59:59', 3),
       (44, 'Tuần 48', '2025-07-07 00:00:00', '2025-07-13 23:59:59', 3),
       (45, 'Tuần 49', '2025-07-14 00:00:00', '2025-07-20 23:59:59', 3),
       (46, 'Tuần 50', '2025-07-21 00:00:00', '2025-07-27 23:59:59', 3),
       (47, 'Tuần 51', '2025-07-28 00:00:00', '2025-08-03 23:59:59', 3),
       (48, 'Tuần 52', '2025-08-04 00:00:00', '2025-08-10 23:59:59', 3),
       (49, 'Tuần 53', '2025-08-11 00:00:00', '2025-08-17 23:59:59', 3),
       (50, 'Tuần 54', '2025-08-18 00:00:00', '2025-08-24 23:59:59', 3),
       (51, 'Tuần 55', '2025-08-25 00:00:00', '2025-08-31 23:59:59', 3),
       (52, 'Tuần 56', '2025-09-01 00:00:00', '2025-09-07 23:59:59', 3),
       (53, 'Tuần 57', '2025-09-08 00:00:00', '2025-09-14 23:59:59', 3),
       (54, 'Tuần 58', '2025-09-15 00:00:00', '2025-09-21 23:59:59', 3),
       (55, 'Tuần 59', '2025-09-22 00:00:00', '2025-09-28 23:59:59', 3),
       (56, 'Tuần 60', '2025-09-29 00:00:00', '2025-10-05 23:59:59', 3),
       (57, 'Tuần 61', '2025-10-06 00:00:00', '2025-10-12 23:59:59', 3);

INSERT INTO subject (id, code, name, total_credits, total_theory_periods, total_practice_periods)
VALUES (1, 'BAS1150', 'Triết học Mác - Lênin', 3, 45, 0),
       (2, 'BAS1203', 'Giải tích 1', 3, 36, 0),
       (3, 'INT1154', 'Tin học cơ sở 1', 2, 20, 4),
       (4, 'BAS1201', 'Đại số', 3, 36, 0),
       (5, 'BAS1106', 'Giáo dục thể chất 1', 2, 2, 0),
       (6, 'BAS1105-7', 'Giáo dục quốc phòng và an ninh', 7, 0, 165),
       (7, 'BAS1151', 'Kinh tế chính trị Mác - Lênin', 2, 30, 0),
       (8, 'BAS1157', 'Tiếng Anh (Course 1)', 4, 60, 0),
       (9, 'BAS1156', 'Tiếng Anh bổ trợ', 4, 60, 0),
       (10, 'BAS1204', 'Giải tích 2', 3, 36, 0),
       (11, 'BAS1224', 'Vật lý 1 và thí nghiệm', 4, 42, 8),
       (12, 'INT1155', 'Tin học cơ sở 2', 2, 20, 4),
       (13, 'ELE1433', 'Kỹ thuật số', 2, 24, 2),
       (14, 'BAS1226', 'Xác suất thống kê', 2, 24, 0),
       (15, 'BAS1107', 'Giáo dục thể chất 2', 2, 2, 26),
       (16, 'BAS1152', 'Chủ nghĩa xã hội khoa học', 2, 30, 0),
       (17, 'BAS1158', 'Tiếng Anh (Course 2)', 4, 60, 0),
       (18, 'INT1358', 'Toán rời rạc 1', 3, 36, 0),
       (19, 'BAS1227', 'Vật lý 3 và thí nghiệm', 4, 36, 4),
       (20, 'ELE1330', 'Xử lý tín hiệu số', 2, 24, 0),
       (21, 'INT1339', 'Ngôn ngữ lập trình C++', 3, 30, 8),
       (22, 'SKD1101', 'Kỹ năng thuyết trình', 1, 6, 0),
       (23, 'BAS1122', 'Tư tưởng Hồ Chí Minh', 2, 24, 0),
       (24, 'BAS1159', 'Tiếng Anh (Course 3)', 4, 60, 0),
       (25, 'INT13145', 'Kiến trúc máy tính', 3, 36, 0),
       (26, 'INT1359-3', 'Toán rời rạc 2', 3, 36, 0),
       (27, 'INT1306', 'Cấu trúc dữ liệu và giải thuật', 3, 32, 4),
       (28, 'ELE1319', 'Lý thuyết thông tin', 3, 36, 0),
       (29, 'SKD1102', 'Kỹ năng làm việc nhóm', 1, 6, 0),
       (30, 'BSA1221', 'Pháp luật đại cương', 2, 24, 0),
       (31, 'BAS1153', 'Lịch sử Đảng Cộng sản Việt Nam', 2, 30, 0),
       (32, 'INT13162', 'Lập trình với Python', 3, 30, 6),
       (33, 'INT1319', 'Hệ điều hành', 3, 34, 3),
       (34, 'INT1336', 'Mạng máy tính', 3, 34, 3),
       (35, 'INT1332', 'Lập trình hướng đối tượng', 3, 30, 6),
       (36, 'INT1313', 'Cơ sở dữ liệu', 3, 32, 4),
       (37, 'BAS1160', 'Tiếng Anh (Course 3 Plus)', 2, 30, 0),
       (38, 'INT1434-3', 'Lập trình Web', 3, 30, 6),
       (39, 'INT1303', 'An toàn và bảo mật hệ thống thông tin', 3, 32, 2),
       (40, 'INT1340', 'Nhập môn công nghệ phần mềm', 3, 36, 0),
       (41, 'INT1341', 'Nhập môn trí tuệ nhân tạo', 3, 36, 0),
       (42, 'INT14148', 'Cơ sở dữ liệu phân tán', 3, 36, 0),
       (43, 'INT13147', 'Thực tập cơ sở', 3, 4, 0),
       (44, 'SKD1103', 'Kỹ năng tạo lập Văn bản', 1, 6, 0);

INSERT INTO course (subject_id, class_id, semester_id, lecturer_id, group_number, total_students)
VALUES (38, 1, 2, 1, 1, 80),
       (39, 1, 2, 2, 1, 86),
       (40, 1, 2, 3, 1, 80),
       (41, 1, 2, 4, 1, 80),
       (42, 1, 2, 5, 1, 80),
       (43, 1, 2, 1, 1, 80),
       (44, 1, 2, 2, 1, 80);

INSERT INTO course_section(course_id, section_number, total_students_in_section)
VALUES (1, 1, 40),
       (1, 2, 40),
       (2, 1, 43),
       (2, 2, 43);


INSERT INTO room (id, name, capacity, status, description, last_updated)
VALUES (1, '2B11', 50, 'AVAILABLE', '', '2025-03-01 10:00:00'),
       (2, '2B12', 50, 'AVAILABLE', '', '2025-03-01 10:30:00'),
       (3, '2B21', 30, 'AVAILABLE', '', '2025-03-01 11:00:00'),
       (4, '2B22', 30, 'AVAILABLE', '', '2025-03-02 08:00:00'),
       (5, '2B31', 50, 'REPAIRING', '', '2025-03-01 12:00:00'),
       (6, '2B32', 50, 'AVAILABLE', '', '2025-03-02 09:00:00');