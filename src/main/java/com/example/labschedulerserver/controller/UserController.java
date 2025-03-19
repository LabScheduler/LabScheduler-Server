package com.example.labschedulerserver.controller;


import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.payload.request.AddLecturerRequest;
import com.example.labschedulerserver.payload.request.AddManagerRequest;
import com.example.labschedulerserver.payload.request.AddStudentRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.payload.response.UserResponse;
import com.example.labschedulerserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.findById(id))
                .message("Get user information successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllUser() {
            DataResponse dataResponse = DataResponse.builder()
                .data(userService.getAllUser())
                .message("Get all account successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/create/manager")
    public ResponseEntity<?> createManagerAccount(@RequestBody AddManagerRequest request) {
        DataResponse dataResponse = DataResponse.builder()
                .message("Create manager account successfully")
                .data(userService.createManagerAccount(request))
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/create/lecturer")
    public ResponseEntity<?> createLecturerAccount(@RequestBody AddLecturerRequest request) {
        DataResponse dataResponse = DataResponse.builder()
                .message("Create lecturer account successfully")
                .data(userService.createLecturerAccount(request))
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/create/student")
    public ResponseEntity<?> createStudentAccount(@RequestBody AddStudentRequest request) {
        DataResponse dataResponse = DataResponse.builder()
                .message("Create student account successfully")
                .data(userService.createStudentAccount(request))
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateUserInfo(@PathVariable Long id, @RequestBody Map<String,Object> payload) {
        Object userInfo = userService.updateUserInfo(id,payload);

        DataResponse dataResponse = DataResponse.builder()
                .data(userInfo)
                .message("Change user information successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PatchMapping("/lock/{id}")
    public ResponseEntity<?> lockAccount(@PathVariable Long id) {
        Account account = userService.lockAccount(id);

        DataResponse dataResponse = DataResponse.builder()
                .message("Lock account successfully")
                .success(true)
                .data(account)
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



}
