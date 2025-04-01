package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.AccountStatus;
import com.example.labschedulerserver.exception.BadRequestException;
import com.example.labschedulerserver.exception.FieldNotFoundException;
import com.example.labschedulerserver.exception.ForbiddenException;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.request.User.AddLecturerRequest;
import com.example.labschedulerserver.payload.request.User.AddManagerRequest;
import com.example.labschedulerserver.payload.request.User.AddStudentRequest;
import com.example.labschedulerserver.payload.response.User.ManagerResponse;
import com.example.labschedulerserver.payload.response.User.UserMapper;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.EmailSenderService;
import com.example.labschedulerserver.service.OtpService;
import com.example.labschedulerserver.service.UserService;
import com.example.labschedulerserver.ultils.ConvertFromJsonToTypeVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final OtpService otpService;
    private final EmailSenderService emailSenderService;

    @Override
    public boolean checkUserIfExist(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public Object findById(Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Object accountInfo = null;
        switch (account.getRole().getName()){
            case "MANAGER" -> {
                accountInfo = managerAccountRepository.findById(userId).get();
            }
            case "LECTURER" -> {
                accountInfo = lecturerAccountRepository.findById(userId).get();
            }
            case "STUDENT" -> {
                accountInfo = studentAccountRepository.findById(userId).get();
            }
        }
        return UserMapper.mapUserToResponse(account, accountInfo);
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public ManagerResponse createManager(AddManagerRequest request) {
        if (accountRepository.existsByEmail(request.getCode() + "@manager.ptithcm.edu.vn")) {
            throw new RuntimeException("User already exists");
        }
        if (request.getCode() == null || request.getCode().isEmpty()) {
            throw new BadRequestException("Code is required");
        }
        if (request.getFullName() == null || request.getFullName().isEmpty()) {
            throw new BadRequestException("Full name is required");
        }
        if (request.getPhone() == null || request.getPhone().isEmpty()) {
            throw new BadRequestException("Phone is required");
        }

        Account account = Account.builder()
                .email(request.getCode() + "@manager.ptithcm.edu.vn")
                .password(passwordEncoder.encode(request.getCode()))
                .role(roleRepository.findRoleByName("MANAGER"))
                .status(AccountStatus.ACTIVE)
                .build();

        ManagerAccount managerAccount = ManagerAccount.builder()
                .fullName(request.getFullName())
                .code(request.getCode())
                .phone(request.getPhone())
                .gender(request.getGender())
                .account(account)
                .build();
        accountRepository.save(account);
        managerAccountRepository.save(managerAccount);
        return (ManagerResponse) UserMapper.mapUserToResponse(account, managerAccount);
    }

    @Override
    public Object createLecturer(AddLecturerRequest request) {
        if (accountRepository.existsByEmail(request.getCode() + "@lecturer.ptithcm.edu.vn")) {
            throw new BadRequestException("User already exists");
        }
        if (request.getCode() == null || request.getCode().isEmpty()) {
            throw new BadRequestException("Code is required");
        }
        if (request.getFullName() == null || request.getFullName().isEmpty()) {
            throw new BadRequestException("Full name is required");
        }
        if (request.getPhone() == null || request.getPhone().isEmpty()) {
            throw new BadRequestException("Phone is required");
        }
        if (request.getDepartmentId() == null) {
            throw new BadRequestException("Department is required");
        }
        Account account = Account.builder()
                .email(request.getCode() + "@lecturer.ptithcm.edu.vn")
                .password(passwordEncoder.encode(request.getCode()))
                .role(roleRepository.findRoleByName("LECTURER"))
                .status(AccountStatus.ACTIVE)
                .build();

        LecturerAccount lecturerAccount = LecturerAccount.builder()
                .account(account)
                .fullName(request.getFullName())
                .code(request.getCode())
                .phone(request.getPhone())
                .gender(request.getGender())
                .department(departmentRepository.findById(request.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId())))
                .build();
        accountRepository.save(account);
        lecturerAccountRepository.save(lecturerAccount);
        return UserMapper.mapUserToResponse(account, lecturerAccount);
    }

    @Override
    public Object createStudent(AddStudentRequest request) {
        if (accountRepository.existsByEmail(request.getCode() + "@student.ptithcm.edu.vn")) {
            throw new BadRequestException("User already exists");
        }
        if (request.getCode() == null || request.getCode().isEmpty()) {
            throw new BadRequestException("Code is required");
        }
        if (request.getFullName() == null || request.getFullName().isEmpty()) {
            throw new BadRequestException("Full name is required");
        }
        if (request.getPhone() == null || request.getPhone().isEmpty()) {
            throw new BadRequestException("Phone is required");
        }
        if (request.getMajorId() == null) {
            throw new BadRequestException("Major is required");
        }
        if (request.getClassId() == null) {
            throw new BadRequestException("Class is required");
        }
        Account account = Account.builder()
                .email(request.getCode() + "@student.ptithcm.edu.vn")
                .password(passwordEncoder.encode(request.getCode()))
                .role(roleRepository.findRoleByName("STUDENT"))
                .status(AccountStatus.ACTIVE)
                .build();
        StudentAccount studentAccount = StudentAccount.builder()
                .account(account)
                .fullName(request.getFullName())
                .code(request.getCode())
                .phone(request.getPhone())
                .gender(request.getGender())
                .clazz(classRepository.findById(request.getClassId()).orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + request.getClassId())))
                .major(majorRepository.findById(request.getMajorId()).orElseThrow(()-> new ResourceNotFoundException("Major not found with id: " + request.getMajorId())))
                .build();
        accountRepository.save(account);
        studentAccountRepository.save(studentAccount);
        return UserMapper.mapUserToResponse(account, studentAccount);
    }

    @Override
    public Object getUserInfo(Long userId) {
        Account user = accountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        switch (user.getRole().getName()){
            case "MANAGER" -> {
                return managerAccountRepository.findById(userId).orElseThrow(RuntimeException::new);
            }
            case "LECTURER" -> {
                return lecturerAccountRepository.findById(userId).orElseThrow(RuntimeException::new);
            }
            case "STUDENT" -> {
                return studentAccountRepository.findById(userId).orElseThrow(RuntimeException::new);
            }
        }
        return null;
    }

@Override
public Object updateUserInfo(Long userId, Map<String, Object> payload) {
    Account user = accountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    Object userInfo = getUserInfo(userId);

    Map<String, Object> idMap = new HashMap<>();
    for (Map.Entry<String, Object> entry : payload.entrySet()) {
        String key = entry.getKey();
        if (key.contains("id")) {
            String mappedKey = key.equals("class_id") ? "clazz" : key.replace("_id", "");
            idMap.put(mappedKey, Long.valueOf(entry.getValue().toString()));
            continue;
        }
        Object value = entry.getValue();
        try {
            Field field = userInfo.getClass().getDeclaredField(ConvertFromJsonToTypeVariable.convert(key));
            field.setAccessible(true);
            field.set(userInfo, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FieldNotFoundException(key + " " + value);
        }
    }

    for (Map.Entry<String, Object> entry : idMap.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        try {
            Field field = userInfo.getClass().getDeclaredField(ConvertFromJsonToTypeVariable.convert(key));
            field.setAccessible(true);
            if (key.contains("department")) {
                Department department = departmentRepository.findById((Long) value).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + value));
                field.set(userInfo, department);
            } else if (key.contains("major")) {
                Major major = majorRepository.findById((Long) value).orElseThrow(() -> new ResourceNotFoundException("Major not found with id: " + value));
                field.set(userInfo, major);
            } else if (key.contains("clazz")) {
                Clazz clazz = classRepository.findById((Long) value).orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + value));
                field.set(userInfo, clazz);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FieldNotFoundException(key + " " + value);
        }
    }

    switch (user.getRole().getName()) {
        case "MANAGER" -> {
            managerAccountRepository.save((ManagerAccount) userInfo);
            return UserMapper.mapUserToResponse(user, userInfo);
        }
        case "LECTURER" -> {
            lecturerAccountRepository.save((LecturerAccount) userInfo);
            return UserMapper.mapUserToResponse(user, userInfo);
        }
        case "STUDENT" -> {
            studentAccountRepository.save((StudentAccount) userInfo);
            return UserMapper.mapUserToResponse(user, userInfo);
        }
        default -> throw new ForbiddenException("idk wtf is this hehe");
    }
}

    @Override
    public List<Object> getAllUser() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(account -> {
            Object accountInfo = null;
            switch (account.getRole().getName()){
                case "MANAGER" -> {
                    accountInfo = managerAccountRepository.findById(account.getId()).get();
                }
                case "LECTURER" -> {
                    accountInfo = lecturerAccountRepository.findById(account.getId()).get();
                }
                case "STUDENT" -> {
                    accountInfo = studentAccountRepository.findById(account.getId()).get();
                }
            }
            return UserMapper.mapUserToResponse(account, accountInfo);
        }).toList();
    }

    @Override
    public void deleteUser(Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        try{
            accountRepository.delete(account);
        }catch (Exception e){
            throw new ForbiddenException("Cannot delete user with id: " + userId);
        }
    }

    @Override
    public Account lockAccount(Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (account.getStatus() == AccountStatus.LOCKED) {
            throw new BadRequestException("Account is already locked");
        }
        account.setStatus(AccountStatus.LOCKED);
        return accountRepository.save(account);
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        Account account = accountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new BadRequestException("New password is required");
        }
        account.setPassword(passwordEncoder.encode(newPassword));
        return true;
    }

    @Override
    public boolean forgotPassword(String email) {
        String otp = otpService.generateOtp(email);
        emailSenderService.sendOtp(email, otp);
        return true;
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        if (!otpService.validateOtp(email, otp)) {
            throw new BadRequestException("Invalid OTP");
        }
        return true;
    }



}
