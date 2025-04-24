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
    public boolean checkUserIfExist(String email);

    public ManagerResponse getCurrentManager();

    public StudentResponse getCurrentStudent();

    public LecturerResponse getCurrentLecturer();


    public Object getUserInfo(Long userId);

    public Object findById(Long userId);
    public Account findByUsername(String username);

    public ManagerResponse createManager(AddManagerRequest request);
    public LecturerResponse createLecturer(AddLecturerRequest request);
    public StudentResponse createStudent(AddStudentRequest request);

    public Object updateUserInfo(Long userId, Map<String, Object> payload);

    public List<Object> getAllUser();

    public void deleteUser(Long userId);

    public Object lockAccount(Long userId);

    public Object unlockAccount(Long userId);

    public boolean changePassword(Long userId, String oldPassword, String newPassword);

    public String forgotPassword(String username);

    public boolean verifyOtp(String username, String otp);

    public boolean resetPassword(String username,String otp, String newPassword);

    public List<Object> filterStudent(Long classId, Long majorId, String code);

    public List<Object> getAllLecturer();
}
