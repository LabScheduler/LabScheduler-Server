INSERT INTO role (name)
    value
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

INSERT INTO Department (id, name) VALUES (1, 'Khoa Công nghệ thông tin');
INSERT INTO Department (id, name) VALUES (2, 'Khoa Kinh tế');
INSERT INTO Department (id, name) VALUES (3, 'Khoa Khoa học xã hội');
INSERT INTO Department (id, name) VALUES (4, 'Khoa Ngoại ngữ');
INSERT INTO Department (id, name) VALUES (5, 'Khoa Luật');

INSERT INTO Major (id, code, name, department_id) VALUES (1, 'CNTT', 'Công nghệ thông tin', 1);
INSERT INTO Major (id, code, name, department_id) VALUES (2, 'KTPM', 'Kỹ thuật phần mềm', 1);
INSERT INTO Major (id, code, name, department_id) VALUES (3, 'QTKD', 'Quản trị kinh doanh', 2);
INSERT INTO Major (id, code, name, department_id) VALUES (4, 'KTKT', 'Kế toán - Kiểm toán', 2);
INSERT INTO Major (id, code, name, department_id) VALUES (5, 'XHNV', 'Xã hội học', 3);
INSERT INTO Major (id, code, name, department_id) VALUES (6, 'TAV', 'Ngôn ngữ Anh', 4);
INSERT INTO Major (id, code, name, department_id) VALUES (7, 'LQT', 'Luật Quốc tế', 5);
INSERT INTO Major (id, code, name, department_id) VALUES (8, 'LDS', 'Luật Dân sự', 5);

INSERT INTO Class ( name,major_id) VALUES ('D22CQCN01-N',1);

INSERT INTO student_account (account_id, full_name, code, phone, gender, major_id, class_id)
VALUES
    (7, 'Ho Cong Toan', 'N22DCCN086', '0123456789',1,1,1);

INSERT INTO manager_account (account_id, full_name, code, phone, gender)
VALUES
    (6, 'Son Dinh', 'MANAGER001', '0123123123',1);

INSERT INTO lecturer_account (account_id, code, full_name, department_id, phone, gender)
VALUES
    (1, 'LECTURER001', 'Huynh Trong Thua', 1, '011111111', 1),
    (2, 'LECTURER002', 'Luu Nguyen Ky Thu', 1, '022222222', 1),
    (3, 'LECTURER003', 'Ha Thanh', 1, '0333333333', 1),
    (4, 'LECTURER004', 'Nguyen Hai', 1, '0444444444', 1),
    (5, 'LECTURER005', 'Nguyen Van Nhat', 1, '0555555555', 1);


INSERT INTO Semester (id, name, start_date, end_date)
VALUES
    (1, '2024-1', '2024-08-01', '2024-12-30'),
    (2, '2024-2', '2024-12-31', '2025-05-31'),
    (3, '2025-1', '2025-08-01', '2025-12-30');

INSERT INTO Semester_Week (id, name, start_date, end_date, semester_id)
VALUES
    (1, 'Tuần 1', '2024-08-12', '2024-08-18', 1),
    (2, 'Tuần 2', '2024-08-19', '2024-08-25', 1),
    (3, 'Tuần 3', '2024-08-26', '2024-09-01', 1),
    (4, 'Tuần 4', '2024-09-02', '2024-09-08', 1),

    (5, 'Tuần 21', '2024-12-30', '2025-01-05', 2),
    (6, 'Tuần 22', '2025-01-06', '2025-01-12', 2),
    (7, 'Tuần 23', '2025-01-13', '2025-01-19', 2),
    (8, 'Tuần 24', '2025-01-20', '2025-01-26', 2);


INSERT INTO Subject (id, code, name, total_credits, total_theory_periods, total_practice_periods)
VALUES
    (1, 'INT1306', 'Cấu trúc dữ liệu và giải thuật', 3, 30, 15),
    (2, 'INT1310', 'Lập trình hướng đối tượng', 4, 40, 20),
    (3, 'INT1402', 'Cơ sở dữ liệu', 3, 35, 10),
    (4, 'INT1206', 'Toán rời rạc', 3, 30, 0),
    (5, 'INT1505', 'Trí tuệ nhân tạo', 4, 45, 10),
    (6, 'INT1601', 'Mạng máy tính', 3, 30, 15),
    (7, 'INT1702', 'Phát triển ứng dụng Web', 4, 40, 20);

INSERT INTO Course (id, subject_id, class_id, semester_id, lecturer_id, total_students)
VALUES
    (1, 1, 1, 1, 1, 80),  -- Cấu trúc dữ liệu và giải thuật, lớp 1, kỳ 2024-1, GV Huynh Trong Thua
    (2, 2, 1, 1, 2, 80),  -- Lập trình hướng đối tượng, lớp 1, kỳ 2024-1, GV Luu Nguyen Ky Thu
    (3, 3, 1, 1, 3, 80), -- Cơ sở dữ liệu, lớp 1, kỳ 2025-1, GV Ha Thanh
    (4, 4, 1, 1, 4, 80), -- Toán rời rạc, lớp 1, kỳ 2025-1, GV Nguyen Hai
    (5, 5, 1, 1, 5, 80), -- Trí tuệ nhân tạo, lớp 1, kỳ 2025-2, GV Nguyen Van Nhat
    (6, 6, 1, 1, 1, 80),  -- Mạng máy tính, lớp 1, kỳ 2025-2, GV Huynh Trong Thua
    (7, 7, 1, 1, 2, 80);  -- Phát triển ứng dụng Web, lớp 1, kỳ 2025-2, GV Luu Nguyen Ky Thu

INSERT INTO Room (id, name, location, capacity, status, description, last_updated)
VALUES
    (1, '2B11', 'B', 40, 'AVAILABLE', '', '2025-03-01 10:00:00'),
    (2, '2B12', 'B', 40, 'AVAILABLE', '', '2025-03-01 10:30:00'),
    (3, '2B21', 'B', 40, 'AVAILABLE', '', '2025-03-01 11:00:00'),
    (4, '2B22', 'B', 40, 'REPAIRING', '', '2025-03-02 08:00:00'),
    (5, '2B31', 'B', 40, 'AVAILABLE', '', '2025-03-01 12:00:00'),
    (6, '2B32', 'B', 40, 'AVAILABLE', '', '2025-03-02 09:00:00');

