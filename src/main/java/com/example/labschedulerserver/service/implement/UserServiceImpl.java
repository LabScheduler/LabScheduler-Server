package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.AccountStatus;
import com.example.labschedulerserver.common.StudentOnClassStatus;
import com.example.labschedulerserver.exception.BadRequestException;
import com.example.labschedulerserver.exception.ForbiddenException;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.request.User.AddLecturerRequest;
import com.example.labschedulerserver.payload.request.User.AddManagerRequest;
import com.example.labschedulerserver.payload.request.User.AddStudentRequest;
import com.example.labschedulerserver.payload.response.User.LecturerResponse;
import com.example.labschedulerserver.payload.response.User.ManagerResponse;
import com.example.labschedulerserver.payload.response.User.StudentResponse;
import com.example.labschedulerserver.payload.response.User.UserMapper;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.ClassService;
import com.example.labschedulerserver.service.EmailSenderService;
import com.example.labschedulerserver.service.OtpService;
import com.example.labschedulerserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private final ClassService classService;
    private final StudentOnClassRepository studentOnClassRepository;

    @Override
    public Object getAccountInfo(Account account) {
        switch (account.getRole().getName()) {
            case "MANAGER" -> {
                return managerAccountRepository.findById(account.getId()).get();
            }
            case "LECTURER" -> {
                return lecturerAccountRepository.findById(account.getId()).get();
            }
            case "STUDENT" -> {
                return studentAccountRepository.findById(account.getId()).get();
            }
        }
        return null;
    }

    @Override
    public boolean checkUserIfExist(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public ManagerResponse getCurrentManager() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        String username = authentication.getName();
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        if (account.getRole().getName().equals("MANAGER")) {
            ManagerAccount managerAccount = managerAccountRepository.findById(account.getId()).orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + account.getId()));
            return (ManagerResponse) UserMapper.mapUserToResponse(account, managerAccount);
        }
        return null;
    }

    @Override
    public StudentResponse getCurrentStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        String username = authentication.getName();
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        if (!Objects.equals(account.getRole().getName(), "STUDENT")) {
            throw new ForbiddenException("User is not a student");
        }
        StudentAccount studentAccount = studentAccountRepository.findById(account.getId()).orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + account.getId()));
        if (account.getRole().getName().equals("STUDENT")) {
            return (StudentResponse) UserMapper.mapUserToResponse(account, studentAccount);
        }
        return null;
    }

    @Override
    public LecturerResponse getCurrentLecturer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        String username = authentication.getName();
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        if (!Objects.equals(account.getRole().getName(), "LECTURER")) {
            throw new ForbiddenException("User is not a lecturer");
        }
        if (account.getRole().getName().equals("LECTURER")) {
            LecturerAccount lecturerAccount = lecturerAccountRepository.findById(account.getId()).orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with id: " + account.getId()));
            return (LecturerResponse) UserMapper.mapUserToResponse(account, lecturerAccount);
        }
        return null;
    }

    @Override
    public Object findById(Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return UserMapper.mapUserToResponse(account, getAccountInfo(account));
    }


    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Override
    @Transactional
    public ManagerResponse createManager(AddManagerRequest request) {
        if (accountRepository.existsByUsername(request.getCode())) {
            throw new RuntimeException("User already exists");
        }

        Account account = Account.builder()
                .username(request.getCode())
                .password(passwordEncoder.encode(request.getCode()))
                .role(roleRepository.findRoleByName("MANAGER"))
                .status(AccountStatus.ACTIVE)
                .build();

        ManagerAccount managerAccount = ManagerAccount.builder()
                .fullName(request.getFullName())
                .code(request.getCode())
                .email(request.getEmail())
                .phone(request.getPhone())
                .gender(request.getGender())
                .account(account)
                .build();

        return (ManagerResponse) UserMapper.mapUserToResponse(accountRepository.save(account), managerAccountRepository.save(managerAccount));
    }

    @Override
    @Transactional
    public LecturerResponse createLecturer(AddLecturerRequest request) {
        Account account = Account.builder()
                .username(request.getCode())
                .password(passwordEncoder.encode(request.getCode()))
                .role(roleRepository.findRoleByName("LECTURER"))
                .status(AccountStatus.ACTIVE)
                .build();

        LecturerAccount lecturerAccount = LecturerAccount.builder()
                .account(account)
                .fullName(request.getFullName())
                .code(request.getCode())
                .email(request.getEmail())
                .phone(request.getPhone())
                .gender(request.getGender())
                .department(departmentRepository.findById(request.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId())))
                .build();
        accountRepository.save(account);
        lecturerAccountRepository.save(lecturerAccount);
        return (LecturerResponse) UserMapper.mapUserToResponse(account, lecturerAccount);
    }

    @Override
    @Transactional
    public StudentResponse createStudent(AddStudentRequest request) {
        if (accountRepository.existsByUsername(request.getCode())) {
            throw new BadRequestException("User already exists");
        }

        Account account = Account.builder()
                .username(request.getCode())
                .password(passwordEncoder.encode(request.getCode()))
                .role(roleRepository.findRoleByName("STUDENT"))
                .status(AccountStatus.ACTIVE)
                .build();

        StudentAccount studentAccount = StudentAccount.builder()
                .account(account)
                .fullName(request.getFullName())
                .code(request.getCode())
                .email(request.getEmail())
                .phone(request.getPhone())
                .gender(request.getGender())
                .build();
        account = accountRepository.save(account);
        studentAccount = studentAccountRepository.save(studentAccount);
        studentAccount.setStudentOnClasses(List.of(StudentOnClass.builder()
                .id(new StudentOnClassId(account.getId(), request.getClassId()))
                .students(studentAccount)
                .clazz(classRepository.findById(request.getClassId()).orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + request.getClassId())))
                .status(StudentOnClassStatus.ENROLLED)
                .build()));
        studentOnClassRepository.save(studentAccount.getStudentOnClasses().getFirst());
        return StudentResponse.builder()
                .major(studentAccount.getStudentOnClasses().getFirst().getClazz().getMajor().getName())
                .specialization("")
                .gender(studentAccount.isGender())
                .id(account.getId())
                .clazz(studentAccount.getStudentOnClasses().getFirst().getClazz().getName())
                .status(account.getStatus().toString())
                .fullName(studentAccount.getFullName())
                .code(studentAccount.getCode())
                .email(studentAccount.getEmail())
                .phone(studentAccount.getPhone())
                .role(account.getRole().getName())
                .build();
    }

    @Override
    @Transactional
    public Object updateUser(Long userId, Map<String, Object> payload) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        try {
            accountRepository.delete(account);
        } catch (Exception e) {
            throw new ForbiddenException("Cannot delete user with id: " + userId);
        }
    }

    @Override
    public Object lockAccount(Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (account.getStatus() == AccountStatus.LOCKED) {
            throw new BadRequestException("Account is already locked");
        }
        account.setStatus(AccountStatus.LOCKED);
        return UserMapper.mapUserToResponse(accountRepository.save(account), getAccountInfo(account));
    }

    @Override
    public Object unlockAccount(Long userId) {
        Account account = accountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (account.getStatus() == AccountStatus.ACTIVE) {
            throw new BadRequestException("Account is already unlocked");
        }
        account.setStatus(AccountStatus.ACTIVE);
        return UserMapper.mapUserToResponse(accountRepository.save(account), getAccountInfo(account));
    }

    @Override
    public Object resetPassword(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + accountId));
        if (account.getStatus() == AccountStatus.LOCKED) {
            throw new BadRequestException("Account is locked");
        }
        account.setPassword(passwordEncoder.encode(account.getUsername()));
        return UserMapper.mapUserToResponse(accountRepository.save(account), getAccountInfo(account));
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        String username = authentication.getName();
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new BadRequestException("New password is required");
        }
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        return true;
    }


    @Override
    public List<Object> filterStudent(Long classId, Long majorId, String code) {
        List<StudentAccount> students = studentAccountRepository.filterStudents(classId, majorId, code);
        if (students.isEmpty()) {
            throw new ResourceNotFoundException("No students found with the given criteria");
        }
        List<Account> accounts = students.stream().map(StudentAccount::getAccount).toList();

        return students.stream().map(student -> {
            Account account = accounts.get(students.indexOf(student));
            return UserMapper.mapUserToResponse(account, student);
        }).toList();
    }

    @Override
    public List<LecturerResponse> getAllLecturer() {
        List<LecturerAccount> lecturers = lecturerAccountRepository.findAll();
        List<Account> accounts = lecturers.stream().map(LecturerAccount::getAccount).toList();

        return lecturers.stream().map(lecturer -> {
            Account account = accounts.get(lecturers.indexOf(lecturer));
            return (LecturerResponse) UserMapper.mapUserToResponse(account, lecturer);
        }).toList();
    }

    @Override
    public List<StudentResponse> getAllStudent() {
        List<StudentAccount> students = studentAccountRepository.findAll().stream().sorted(Comparator.comparing(StudentAccount::getFullName)).toList();
        List<Account> accounts = students.stream().map(StudentAccount::getAccount).toList();
        return students.stream().map(student -> {
            Account account = accounts.get(students.indexOf(student));
            return (StudentResponse) UserMapper.mapUserToResponse(account, student);
        }).toList();
    }
}
