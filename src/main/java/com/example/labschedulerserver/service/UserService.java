package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.payload.request.User.AddLecturerRequest;
import com.example.labschedulerserver.payload.request.User.AddManagerRequest;
import com.example.labschedulerserver.payload.request.User.AddStudentRequest;
import com.example.labschedulerserver.payload.response.User.LecturerResponse;
import com.example.labschedulerserver.payload.response.User.ManagerResponse;
import com.example.labschedulerserver.payload.response.User.StudentResponse;

import java.util.List;
import java.util.Map;

public interface UserService {
    public Object getAccountInfo(Account account);

    public boolean checkUserIfExist(String username);

    public ManagerResponse getCurrentManager();

    public StudentResponse getCurrentStudent();

    public LecturerResponse getCurrentLecturer();


    public Object findById(Long userId);

    public Account findByUsername(String username);

    public ManagerResponse createManager(AddManagerRequest request);
    public LecturerResponse createLecturer(AddLecturerRequest request);
    public StudentResponse createStudent(AddStudentRequest request);

    public Object updateUser(Long userId, Map<String, Object> payload);

    public List<StudentResponse> getAllStudent();
    public List<LecturerResponse> getAllLecturer();

    public void deleteUser(Long userId);

    public Object lockAccount(Long userId);

    public Object unlockAccount(Long userId);

    public Object resetPassword(Long accountId);

    public boolean changePassword(String oldPassword, String newPassword);

    public List<Object> filterStudent(Long classId, Long majorId, String code);
}
