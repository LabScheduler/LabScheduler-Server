package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.AccountStatus;
import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.model.LecturerAccount;
import com.example.labschedulerserver.model.ManagerAccount;
import com.example.labschedulerserver.model.StudentAccount;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.payload.response.UserResponse;
import com.example.labschedulerserver.repository.AccountRepository;
import com.example.labschedulerserver.repository.LecturerAccountRepository;
import com.example.labschedulerserver.repository.ManagerAccountRepository;
import com.example.labschedulerserver.repository.StudentAccountRepository;
import com.example.labschedulerserver.service.UserService;
import com.example.labschedulerserver.ultils.ConvertFromJsonToTypeVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public Account createAccount(Account account) {
        accountRepository.save(account);
        return null;
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<Account> accounts = accountRepository.findAll();
        List<UserResponse> result = new ArrayList<>();
        for(Account account : accounts){
            UserResponse userResponse = UserResponse.builder()
                    .email(account.getEmail())
                    .role(account.getRole().getName())
                    .id(account.getId())
                    .status(account.getStatus())
                    .userInfo(getUserInfo(account))
                    .build();
            result.add(userResponse);
        }
        return result;
    }

    @Override
    public void deleteUser(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        accountRepository.delete(account);
    }

    @Override
    public Account lockAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()-> new RuntimeException("Account not found"));
        account.setStatus(AccountStatus.valueOf("LOCKED"));
        accountRepository.save(account);

        return account;
    }

    @Override
    public Object getUserInfo(Account account) {
        Object userInfo = null;
        switch (account.getRole().getName()){
            case "MANAGER":
                userInfo = managerAccountRepository.findById(account.getId()).orElseThrow(() -> new RuntimeException("Manager not found"));
                break;
            case "LECTURER":
                userInfo = lecturerAccountRepository.findById(account.getId()).orElseThrow(() -> new RuntimeException("Lecturer not found"));
                break;
            case "STUDENT":
                userInfo = studentAccountRepository.findById(account.getId()).orElseThrow(() -> new RuntimeException("Student not found"));
                break;
        }

        return userInfo;
    }

    public void fieldSet(Object object){

    }
    @Override
    public Object updateUserInfo(Long id, Map<String, Object> payload) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        Object userInfo = getUserInfo(account);

        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                Field field = userInfo.getClass().getDeclaredField(ConvertFromJsonToTypeVariable.convert(key));
                field.setAccessible(true);
                field.set(userInfo, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Field not found or inaccessible: " + key, e);
            }
        }

        switch (account.getRole().getName()) {
            case "MANAGER":
                managerAccountRepository.save((ManagerAccount) userInfo);
                break;
            case "LECTURER":
                lecturerAccountRepository.save((LecturerAccount) userInfo);
                break;
            case "STUDENT":
                studentAccountRepository.save((StudentAccount) userInfo);
                break;
        }

        return userInfo;
    }

}
