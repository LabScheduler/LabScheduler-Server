package com.example.labschedulerserver.controller;


import com.example.labschedulerserver.payload.request.User.AddLecturerRequest;
import com.example.labschedulerserver.payload.request.User.AddManagerRequest;
import com.example.labschedulerserver.payload.request.User.AddStudentRequest;
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

    @GetMapping
    public ResponseEntity<?> getAllUser() {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.getAllUser())
                .message("Get all user successfully")
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

    @PostMapping("/create/manager")
    public ResponseEntity<?> createManager(@RequestBody AddManagerRequest request) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.createManager(request))
                .message("Create manager successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/create/lecturer")
    public ResponseEntity<?> createLecturer(@RequestBody AddLecturerRequest request) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.createLecturer(request))
                .message("Create lecturer successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/create/student")
    public ResponseEntity<?> createStudent(@RequestBody AddStudentRequest request) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.createStudent(request))
                .message("Create student successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateUserInfo(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.updateUserInfo(id, payload))
                .message("Update user information successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @DeleteMapping("/delete/{id}")
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
    public ResponseEntity<?> changePassword(@RequestParam String email, @RequestParam(value = "old_password") String oldPassword, @RequestParam(value = "new_password") String newPassword) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.changePassword(email, oldPassword, newPassword))
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
}
