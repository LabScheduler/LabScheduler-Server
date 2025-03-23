package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.AccountStatus;
import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.model.LecturerAccount;
import com.example.labschedulerserver.model.ManagerAccount;
import com.example.labschedulerserver.model.StudentAccount;
import com.example.labschedulerserver.payload.request.AddLecturerRequest;
import com.example.labschedulerserver.payload.request.AddManagerRequest;
import com.example.labschedulerserver.payload.request.AddStudentRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.payload.response.UserResponse;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.UserService;
import com.example.labschedulerserver.ultils.ConvertFromJsonToTypeVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final DepartmentRepository departmentRepository;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final MajorRepository majorRepository;
    private final ClassRepository classRepository;

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
    public UserResponse createManagerAccount(AddManagerRequest request) {
        accountRepository.findUserByEmail(request.getEmail()).ifPresent(user->{
            throw new RuntimeException("User with email: " + request.getEmail() + " already exists");
        });
        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleRepository.findRoleByName("MANAGER"))
                .status(AccountStatus.ACTIVE)
                .build();
        ManagerAccount managerAccount = ManagerAccount.builder()
                .account(account)
                .fullName(request.getFullName())
                .code(request.getCode())
                .phone(request.getPhone())
                .gender(request.getGender())
                .build();
        accountRepository.save(account);
        managerAccountRepository.save(managerAccount);
        return UserResponse.builder()
                .userInfo(managerAccount)
                .email(account.getEmail())
                .role(account.getRole().getName())
                .status(AccountStatus.ACTIVE)
                .id(account.getId())
                .build();
    }

    @Override
    public UserResponse createLecturerAccount(AddLecturerRequest request) {
        accountRepository.findUserByEmail(request.getEmail()).ifPresent(user->{
            throw new RuntimeException("User with email: " + request.getEmail() + " already exists");
        });
        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleRepository.findRoleByName("LECTURER"))
                .status(AccountStatus.ACTIVE)
                .build();

        LecturerAccount lecturerAccount = LecturerAccount.builder()
                .account(account)
                .fullName(request.getFullName())
                .code(request.getCode())
                .phone(request.getPhone())
                .gender(request.getGender())
                .department(departmentRepository.findById(Long.valueOf(request.getDepartmentId())).orElseThrow(()-> new RuntimeException("Department not found")))
                .build();

        accountRepository.save(account);
        lecturerAccountRepository.save(lecturerAccount);
        return UserResponse.builder()
                .userInfo(lecturerAccount)
                .email(account.getEmail())
                .role(account.getRole().getName())
                .status(AccountStatus.ACTIVE)
                .id(account.getId())
                .build();
    }

    @Override
    public UserResponse createStudentAccount(AddStudentRequest request) {
        accountRepository.findUserByEmail(request.getEmail()).ifPresent(user->{
            throw new RuntimeException("User with email: " + request.getEmail() + " already exists");
        });
        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleRepository.findRoleByName("STUDENT"))
                .status(AccountStatus.ACTIVE)
                .build();
        StudentAccount studentAccount = StudentAccount.builder()
                .account(account)
                .fullName(request.getFullName())
                .code(request.getCode())
                .phone(request.getPhone())
                .gender(request.getGender())
                .major(majorRepository.findById(Long.valueOf(request.getMajorId())).orElseThrow(()-> new RuntimeException("Major not found")))
                .clazz(classRepository.findById(Long.valueOf(request.getClassId())).orElseThrow(()-> new RuntimeException("Class not found")))
                .build();
        accountRepository.save(account);
        studentAccountRepository.save(studentAccount);
        return UserResponse.builder()
                .userInfo(studentAccount)
                .email(account.getEmail())
                .role(account.getRole().getName())
                .status(AccountStatus.ACTIVE)
                .id(account.getId())
                .build();
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
    public UserResponse findById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        return  UserResponse.builder()
                .email(account.getEmail())
                .role(account.getRole().getName())
                .id(account.getId())
                .status(account.getStatus())
                .userInfo(getUserInfo(account))
                .build();
    }

    @Override
    public Object getUserInfo(Account account) {
        return switch (account.getRole().getName()) {
            case "MANAGER" ->
                    managerAccountRepository.findById(account.getId()).orElseThrow(() -> new RuntimeException("Manager not found"));
            case "LECTURER" ->
                    lecturerAccountRepository.findById(account.getId()).orElseThrow(() -> new RuntimeException("Lecturer not found"));
            case "STUDENT" ->
                    studentAccountRepository.findById(account.getId()).orElseThrow(() -> new RuntimeException("Student not found"));
            default -> null;
        };
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
