package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.model.LecturerAccount;
import com.example.labschedulerserver.model.ManagerAccount;
import com.example.labschedulerserver.model.StudentAccount;
import com.example.labschedulerserver.repository.AccountRepository;
import com.example.labschedulerserver.repository.LecturerAccountRepository;
import com.example.labschedulerserver.repository.ManagerAccountRepository;
import com.example.labschedulerserver.repository.StudentAccountRepository;
import com.example.labschedulerserver.service.UserService;
import com.example.labschedulerserver.ultils.ConvertFromJsonToTypeVariable;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Manager;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
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
    public void saveUser(Account account) {
        accountRepository.save(account);
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
    public Object changeUserInfo(Integer id, Map<String, Object> payload) {
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
