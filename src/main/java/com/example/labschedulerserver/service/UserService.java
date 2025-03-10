package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.payload.request.GetUserRequest;
import com.example.labschedulerserver.payload.response.UserInfoResponse;

import java.util.Optional;

public interface UserService {
    public String checkUserIfExist(String email);
    public Optional<Account> getUserByEmail(String email);
    public void saveUser(Account account);
    public UserInfoResponse getUserInfo(GetUserRequest getUserRequest);

}
