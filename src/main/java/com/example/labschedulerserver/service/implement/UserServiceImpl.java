package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.payload.request.GetUserRequest;
import com.example.labschedulerserver.payload.response.UserInfoResponse;
import com.example.labschedulerserver.repository.AccountRepository;
import com.example.labschedulerserver.repository.LecturerAccountRepository;
import com.example.labschedulerserver.repository.ManagerAccountRepository;
import com.example.labschedulerserver.repository.StudentAccountRepository;
import com.example.labschedulerserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AccountRepository accountRepository;
    private final ManagerAccountRepository managerAccountRepository;
    private final LecturerAccountRepository lecturerAccountRepository;
    private final StudentAccountRepository studentAccountRepository;

    @Override
    public String checkUserIfExist(String email) {
        Account account = accountRepository.findUserByEmail(email).orElse(null);
        if(account == null) {
            return null;
        }
        return account.getEmail();
    }

    @Override
    public Optional<Account> getUserByEmail(String email) {
        return accountRepository.findUserByEmail(email);
    }

    @Override
    public void saveUser(Account account) {
        accountRepository.save(account);
    }

    @Override
    public UserInfoResponse getUserInfo(GetUserRequest getUserRequest) {
        Account account = accountRepository.findAccountByEmail(String.valueOf(getUserRequest.getEmail())).orElseThrow(() -> new RuntimeException("Account not found"));
        switch (getUserRequest.getRole()) {
            case "MANAGER":
                return UserInfoResponse.builder()
                        .id(account.getId())
                        .email(account.getEmail())
                        .role(getUserRequest.getRole())
                        .status(account.getStatus())
                        .accountInfo(managerAccountRepository.findByAccount(account))
                        .build();
            case "LECTURER":
                return UserInfoResponse.builder()
                        .id(account.getId())
                        .email(account.getEmail())
                        .role(getUserRequest.getRole())
                        .status(account.getStatus())
                        .accountInfo(lecturerAccountRepository.findByAccount((account)))
                        .build();
            case "STUDENT":
                return UserInfoResponse.builder()
                        .id(account.getId())
                        .email(account.getEmail())
                        .role(getUserRequest.getRole())
                        .status(account.getStatus())
                        .accountInfo(studentAccountRepository.findByAccount(account))
                        .build();
        }
        return null;
    }


}
