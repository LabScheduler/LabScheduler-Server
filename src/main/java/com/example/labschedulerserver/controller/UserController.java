package com.example.labschedulerserver.controller;


import com.example.labschedulerserver.payload.request.User.AddLecturerRequest;
import com.example.labschedulerserver.payload.request.User.AddManagerRequest;
import com.example.labschedulerserver.payload.request.User.AddStudentRequest;
import com.example.labschedulerserver.payload.request.User.UpdateUserProfileRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudent() {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.getAllStudent())
                .message("Get all student successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/lecturers")
    public ResponseEntity<?> getAllLecturer() {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.getAllLecturer())
                .message("Get all lecturer successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.findById(id))
                .message("Get user information successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/manager")
    public ResponseEntity<?> createManager(@RequestBody AddManagerRequest request) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.createManager(request))
                .message("Create manager successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/lecturer")
    public ResponseEntity<?> createLecturer(@RequestBody AddLecturerRequest request) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.createLecturer(request))
                .message("Create lecturer successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/student")
    public ResponseEntity<?> createStudent(@RequestBody AddStudentRequest request) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.createStudent(request))
                .message("Create student successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PutMapping
    public ResponseEntity<?> updateUserInfo(@RequestBody UpdateUserProfileRequest request) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.updateUser(request))
                .message("Update user information successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PatchMapping("/student/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.updateStudent(id, payload))
                .message("Update student successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }
    @PatchMapping("/lecturer/{id}")
    public ResponseEntity<?> updateLecturer(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.updateLecturer(id, payload))
                .message("Update lecturer successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        DataResponse dataResponse = DataResponse.builder()
                .message("Delete user successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PatchMapping("/lock/{id}")
    public ResponseEntity<?> lockAccount(@PathVariable Long id) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.lockAccount(id))
                .message("Lock account successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }



    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam(value = "old_password") String oldPassword, @RequestParam(value = "new_password") String newPassword) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.changePassword(oldPassword, newPassword))
                .message("Change password successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/filter-student")
    public ResponseEntity<?> filterStudent(
            @RequestParam(value = "class_id", required = false) Long classId,
            @RequestParam(value = "major_id", required = false) Long majorId,
            @RequestParam(value = "code", required = false) String code
    ) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.filterStudent(classId, majorId, code))
                .message("Filter student successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PatchMapping("/unlock/{id}")
    public ResponseEntity<?> unlockAccount(@PathVariable Long id) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.unlockAccount(id))
                .message("Unlock account successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/get-current-manager")
    public ResponseEntity<?> getCurrentManager() {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.getCurrentManager())
                .message("Get current manager successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/get-current-lecturer")
    public ResponseEntity<?> getCurrentLecturer() {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.getCurrentLecturer())
                .message("Get current lecturer successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }
    @GetMapping("/get-current-student")
    public ResponseEntity<?> getCurrentStudent() {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.getCurrentStudent())
                .message("Get current student successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }
}
