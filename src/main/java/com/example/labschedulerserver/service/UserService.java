package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.payload.response.UserResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    public String checkUserIfExist(String email);
    public Optional<Account> getUserByEmail(String email);
    public void saveUser(Account account);
    public Object getUserInfo(Account account);
    public Object updateUserInfo(Integer id, Map<String, Object> payload);
    public List<UserResponse> getAllUser();
    public void deleteUser(Integer id);
    public Account lockAccount(Integer id);

}
