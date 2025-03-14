package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Account;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    public String checkUserIfExist(String email);
    public Optional<Account> getUserByEmail(String email);
    public void saveUser(Account account);
    public Object getUserInfo(Account account);
    public boolean changeUserInfo(Integer id, String role, Map<String, Object> payload);

}
