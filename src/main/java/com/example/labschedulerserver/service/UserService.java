package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.payload.request.User.AddLecturerRequest;
import com.example.labschedulerserver.payload.request.User.AddManagerRequest;
import com.example.labschedulerserver.payload.request.User.AddStudentRequest;
import com.example.labschedulerserver.payload.response.User.ManagerResponse;
import com.example.labschedulerserver.payload.response.User.StudentResponse;

import java.util.List;
import java.util.Map;

public interface UserService {
    public boolean checkUserIfExist(String email);

    public Object getUserInfo(Long userId);

    public Object findById(Long userId);
    public Account findByEmail(String email);

    public ManagerResponse createManager(AddManagerRequest request);
    public Object createLecturer(AddLecturerRequest request);
    public Object createStudent(AddStudentRequest request);

    public Object updateUserInfo(Long userId, Map<String, Object> payload);

    public List<Object> getAllUser();

    public void deleteUser(Long userId);

    public Object lockAccount(Long userId);

    public Object unlockAccount(Long userId);

    public boolean changePassword(Long userId, String oldPassword, String newPassword);

    public boolean forgotPassword(String email);

    public boolean verifyOtp(String email, String otp);

    public boolean resetPassword(String email,String otp, String newPassword);

    public boolean changePassword(String email, String oldPassword, String newPassword);

    public List<Object> filterStudent(Long classId, Long majorId, String code);

    public List<Object> getAllLecturer();
}
