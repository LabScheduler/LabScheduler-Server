package com.example.labschedulerserver.payload.response.User;

import com.example.labschedulerserver.common.StudentOnClassStatus;
import com.example.labschedulerserver.exception.ForbiddenException;
import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.model.LecturerAccount;
import com.example.labschedulerserver.model.ManagerAccount;
import com.example.labschedulerserver.model.StudentAccount;

public class UserMapper {
    public static Object mapUserToResponse(Account account, Object accountInfo) {
        return switch (account.getRole().getName()) {
            case "MANAGER" -> {
                ManagerAccount managerAccount = (ManagerAccount) accountInfo;
                yield ManagerResponse.builder()
                        .id(account.getId())
                        .fullName(managerAccount.getFullName())
                        .code(managerAccount.getCode())
                        .phone(managerAccount.getPhone())
                        .email(((ManagerAccount) accountInfo).getEmail())
                        .role(account.getRole().getName())
                        .status(account.getStatus().name())
                        .gender(managerAccount.isGender())
                        .birthday(managerAccount.getBirthday())
                        .build();
            }
            case "LECTURER" -> {
                LecturerAccount lecturerAccount = (LecturerAccount) accountInfo;
                yield LecturerResponse.builder()
                        .id(account.getId())
                        .fullName(lecturerAccount.getFullName())
                        .code(lecturerAccount.getCode())
                        .phone(lecturerAccount.getPhone())
                        .email(((LecturerAccount) accountInfo).getEmail())
                        .role(account.getRole().getName())
                        .status(account.getStatus().name())
                        .gender(lecturerAccount.isGender())
                        .department(lecturerAccount.getDepartment().getName())
                        .birthday(lecturerAccount.getBirthday())
                        .build();
            }
            case "STUDENT" -> {
                StudentAccount studentAccount = (StudentAccount) accountInfo;
                yield StudentResponse.builder()
                        .id(account.getId())
                        .fullName(studentAccount.getFullName())
                        .code(studentAccount.getCode())
                        .phone(studentAccount.getPhone())
                        .email(((StudentAccount) accountInfo).getEmail())
                        .gender(studentAccount.isGender())
                        .role(account.getRole().getName())
                        .status(account.getStatus().name())
                        .clazz(
                                studentAccount.getStudentOnClasses().stream()
                                        .filter(studentOnClass -> studentOnClass.getStatus() == StudentOnClassStatus.ENROLLED)
                                        .findFirst()
                                        .map(studentOnClass -> studentOnClass.getClazz().getName())
                                        .orElse("")
                        )
                        .major(studentAccount.getStudentOnClasses().getFirst().getClazz().getMajor().getName())
                        .specialization(studentAccount.getStudentOnClasses().stream()
                                .filter(studentOnClass -> studentOnClass.getStatus() == StudentOnClassStatus.ENROLLED)
                                .map(studentOnClass -> studentOnClass.getClazz().getSpecialization() !=null ? studentOnClass.getClazz().getSpecialization().getName() : "")
                                .findFirst()
                                .orElse(""))
                        .birthday(studentAccount.getBirthday())
                        .build();
            }
            default -> throw new ForbiddenException("Invalid role");
        };
    }
}
