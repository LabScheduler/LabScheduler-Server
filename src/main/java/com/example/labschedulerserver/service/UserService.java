package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.payload.request.AddLecturerRequest;
import com.example.labschedulerserver.payload.request.AddManagerRequest;
import com.example.labschedulerserver.payload.request.AddStudentRequest;
import com.example.labschedulerserver.payload.response.UserResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    public String checkUserIfExist(String email);
    public Optional<Account> getUserByEmail(String email);
    public UserResponse createManagerAccount(AddManagerRequest request);
    public UserResponse createLecturerAccount(AddLecturerRequest request);
    public UserResponse createStudentAccount(AddStudentRequest request);
    public Object getUserInfo(Account account);
    public Object updateUserInfo(Long id, Map<String, Object> payload);
    public List<UserResponse> getAllUser();
    public void deleteUser(Long id);
    public Account lockAccount(Long id);
    public UserResponse findById(Long id);
}
