INSERT INTO role (name)
    VALUE
    ('MANAGER'),
    ('LECTURER'),
    ('STUDENT');

INSERT INTO account (email, password, role, status)
VALUES
    ('gv1@lecturer.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'LECTURER', 'ACTIVE'),
    ('gv2@lecturer.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'LECTURER', 'ACTIVE'),
    ('gv3@lecturer.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'LECTURER', 'ACTIVE'),
    ('gv4@lecturer.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'LECTURER', 'ACTIVE'),
    ('gv5@lecturer.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'LECTURER', 'ACTIVE'),
    ('manager1@manager.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'MANAGER', 'ACTIVE'),
    ('n22dccn086@student.ptithcm.edu.vn', '$2a$12$VR1KnMqjnuEkyUtl5se/oOTqGu.BoCPO3weJA7OLmg4JtkNzLNtPO', 'STUDENT', 'ACTIVE');

INSERT INTO department (id, name) VALUES (1, 'Khoa Công nghệ thông tin');
INSERT INTO department (id, name) VALUES (2, 'Khoa Kinh tế');
INSERT INTO department (id, name) VALUES (3, 'Khoa Khoa học xã hội');
INSERT INTO department (id, name) VALUES (4, 'Khoa Ngoại ngữ');
INSERT INTO department (id, name) VALUES (5, 'Khoa Luật');

INSERT INTO major (id, code, name, department_id) VALUES (1, 'CNTT', 'Công nghệ thông tin', 1);
INSERT INTO major (id, code, name, department_id) VALUES (2, 'KTPM', 'Kỹ thuật phần mềm', 1);
INSERT INTO major (id, code, name, department_id) VALUES (3, 'QTKD', 'Quản trị kinh doanh', 2);
INSERT INTO major (id, code, name, department_id) VALUES (4, 'KTKT', 'Kế toán - Kiểm toán', 2);
INSERT INTO major (id, code, name, department_id) VALUES (5, 'XHNV', 'Xã hội học', 3);
INSERT INTO major (id, code, name, department_id) VALUES (6, 'TAV', 'Ngôn ngữ Anh', 4);
INSERT INTO major (id, code, name, department_id) VALUES (7, 'LQT', 'Luật Quốc tế', 5);
INSERT INTO major (id, code, name, department_id) VALUES (8, 'LDS', 'Luật Dân sự', 5);

INSERT INTO class (name, major_id) VALUES ('D22CQCN01-N', 1);

INSERT INTO student_account (account_id, full_name, code, phone, gender, major_id, class_id)
VALUES
    (7, 'Ho Cong Toan', 'N22DCCN086', '0123456789', 1, 1, 1);

INSERT INTO manager_account (account_id, full_name, code, phone, gender)
VALUES
    (6, 'Son Dinh', 'MANAGER001', '0123123123', 1);

INSERT INTO lecturer_account (account_id, code, full_name, department_id, phone, gender)
VALUES
    (1, 'LECTURER001', 'Huynh Trong Thua', 1, '011111111', 1),
    (2, 'LECTURER002', 'Luu Nguyen Ky Thu', 1, '022222222', 1),
    (3, 'LECTURER003', 'Ha Thanh', 1, '0333333333', 1),
    (4, 'LECTURER004', 'Nguyen Hai', 1, '0444444444', 1),
    (5, 'LECTURER005', 'Nguyen Van Nhat', 1, '0555555555', 1);

INSERT INTO semester (id, name, start_date, end_date)
VALUES
    (1, '2024-1', '2024-08-01', '2024-12-30'),
    (2, '2024-2', '2024-12-31', '2025-05-31'),
    (3, '2025-1', '2025-08-01', '2025-12-30');

INSERT INTO semester_week (id, name, start_date, end_date, semester_id)
VALUES
    (1, 'Tuần 1', '2024-08-05', '2024-08-11', 1),
    (2, 'Tuần 2', '2024-08-12', '2024-08-18', 1),
    (3, 'Tuần 3', '2024-08-19', '2024-08-25', 1),
    (4, 'Tuần 4', '2024-08-26', '2024-09-01', 1),
    (5, 'Tuần 5', '2024-09-02', '2024-09-08', 1),
    (6, 'Tuần 6', '2024-09-09', '2024-09-15', 1),
    (7, 'Tuần 7', '2024-09-16', '2024-09-22', 1),
    (8, 'Tuần 8', '2024-09-23', '2024-09-29', 1),
    (9, 'Tuần 9', '2024-09-30', '2024-10-06', 1),
    (10, 'Tuần 10', '2024-10-07', '2024-10-13', 1),
    (11, 'Tuần 11', '2024-10-14', '2024-10-20', 1),
    (12, 'Tuần 12', '2024-10-21', '2024-10-27', 1),
    (13, 'Tuần 13', '2024-10-28', '2024-11-03', 1),
    (14, 'Tuần 14', '2024-11-04', '2024-11-10', 1),
    (15, 'Tuần 15', '2024-11-11', '2024-11-17', 1),
    (16, 'Tuần 16', '2024-11-18', '2024-11-24', 1),
    (17, 'Tuần 17', '2024-11-25', '2024-12-01', 1),
    (18, 'Tuần 18', '2024-12-02', '2024-12-08', 1),
    (19, 'Tuần 19', '2024-12-09', '2024-12-15', 1),
    (20, 'Tuần 20', '2024-12-16', '2024-12-22', 1),
    (21, 'Tuần 21', '2024-12-23', '2024-12-29', 1),
    (22, 'Tuần 22', '2024-12-30', '2025-01-05', 1),
    (23, 'Tuần 23', '2025-01-06', '2025-01-12', 2),
    (24, 'Tuần 24', '2025-01-13', '2025-01-19', 2),
    (25, 'Tuần 25', '2025-01-20', '2025-01-26', 2),
    (26, 'Tuần 26', '2025-01-27', '2025-02-02', 2),
    (27, 'Tuần 27', '2025-02-03', '2025-02-09', 2),
    (28, 'Tuần 28', '2025-02-10', '2025-02-16', 2),
    (29, 'Tuần 29', '2025-02-17', '2025-02-23', 2),
    (30, 'Tuần 30', '2025-02-24', '2025-03-02', 2),
    (31, 'Tuần 31', '2025-03-03', '2025-03-09', 2),
    (32, 'Tuần 32', '2025-03-10', '2025-03-16', 2),
    (33, 'Tuần 33', '2025-03-17', '2025-03-23', 2),
    (34, 'Tuần 34', '2025-03-24', '2025-03-30', 2),
    (35, 'Tuần 35', '2025-03-31', '2025-04-06', 2),
    (36, 'Tuần 36', '2025-04-07', '2025-04-13', 2),
    (37, 'Tuần 37', '2025-04-14', '2025-04-20', 2),
    (38, 'Tuần 38', '2025-04-21', '2025-04-27', 2),
    (39, 'Tuần 39', '2025-04-28', '2025-05-04', 2),
    (40, 'Tuần 40', '2025-05-05', '2025-05-11', 2),
    (41, 'Tuần 41', '2025-05-12', '2025-05-18', 2),
    (42, 'Tuần 42', '2025-05-19', '2025-05-25', 2),
    (43, 'Tuần 43', '2025-05-26', '2025-06-01', 2),
    (44, 'Tuần 44', '2025-08-04', '2025-08-10', 3),
    (45, 'Tuần 45', '2025-08-11', '2025-08-17', 3),
    (46, 'Tuần 46', '2025-08-18', '2025-08-24', 3),
    (47, 'Tuần 47', '2025-08-25', '2025-08-31', 3),
    (48, 'Tuần 48', '2025-09-01', '2025-09-07', 3),
    (49, 'Tuần 49', '2025-09-08', '2025-09-14', 3),
    (50, 'Tuần 50', '2025-09-15', '2025-09-21', 3),
    (51, 'Tuần 51', '2025-09-22', '2025-09-28', 3),
    (52, 'Tuần 52', '2025-09-29', '2025-10-05', 3),
    (53, 'Tuần 53', '2025-10-06', '2025-10-12', 3),
    (54, 'Tuần 54', '2025-10-13', '2025-10-19', 3),
    (55, 'Tuần 55', '2025-10-20', '2025-10-26', 3),
    (56, 'Tuần 56', '2025-10-27', '2025-11-02', 3),
    (57, 'Tuần 57', '2025-11-03', '2025-11-09', 3),
    (58, 'Tuần 58', '2025-11-10', '2025-11-16', 3),
    (59, 'Tuần 59', '2025-11-17', '2025-11-23', 3),
    (60, 'Tuần 60', '2025-11-24', '2025-11-30', 3);

INSERT INTO subject (id, code, name, total_credits, total_theory_periods, total_practice_periods)
VALUES
    (1, 'INT1306', 'Cấu trúc dữ liệu và giải thuật', 3, 30, 15),
    (2, 'INT1310', 'Lập trình hướng đối tượng', 4, 40, 20),
    (3, 'INT1402', 'Cơ sở dữ liệu', 3, 35, 10),
    (4, 'INT1206', 'Toán rời rạc', 3, 30, 0),
    (5, 'INT1505', 'Trí tuệ nhân tạo', 4, 45, 10),
    (6, 'INT1601', 'Mạng máy tính', 3, 30, 15),
    (7, 'INT1702', 'Phát triển ứng dụng Web', 4, 40, 20);

INSERT INTO course (subject_id, class_id, semester_id, lecturer_id,group_number, total_students)
VALUES
    (1, 1, 1, 1,1 ,80),  -- Cấu trúc dữ liệu và giải thuật, lớp 1, kỳ 2024-1, GV Huynh Trong Thua
    (2, 1, 1, 2, 1,80),  -- Lập trình hướng đối tượng, lớp 1, kỳ 2024-1, GV Luu Nguyen Ky Thu
    (3, 1, 1, 3, 1,180),  -- Cơ sở dữ liệu, lớp 1, kỳ 2025-1, GV Ha Thanh
    (4, 1, 1, 4, 1,80),  -- Toán rời rạc, lớp 1, kỳ 2025-1, GV Nguyen Hai
    (5, 1, 1, 5, 1,80),  -- Trí tuệ nhân tạo, lớp 1, kỳ 2025-2, GV Nguyen Van Nhat
    (6, 1, 1, 1, 1,80),  -- Mạng máy tính, lớp 1, kỳ 2025-2, GV Huynh Trong Thua
    (7, 1, 1, 2, 1,80);  -- Phát triển ứng dụng Web, lớp 1, kỳ 2025-2, GV Luu Nguyen Ky Thu

INSERT INTO room (id, name, capacity, status, description, last_updated)
VALUES
    (1, '2B11', 40, 'AVAILABLE', '', '2025-03-01 10:00:00'),
    (2, '2B12', 40, 'AVAILABLE', '', '2025-03-01 10:30:00'),
    (3, '2B21', 40, 'AVAILABLE', '', '2025-03-01 11:00:00'),
    (4, '2B22', 40, 'REPAIRING', '', '2025-03-02 08:00:00'),
    (5, '2B31', 40, 'AVAILABLE', '', '2025-03-01 12:00:00'),
    (6, '2B32', 40, 'AVAILABLE', '', '2025-03-02 09:00:00');