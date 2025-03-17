package com.example.labschedulerserver.controller;


import com.example.labschedulerserver.model.Account;
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


    @GetMapping
    public ResponseEntity<?> getAllUser() {
        List<UserResponse> result = userService.getAllUser();

        DataResponse dataResponse = DataResponse.builder()
                .data(result)
                .message("Get all account successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateUserInfo(@PathVariable Integer id, @RequestBody Map<String,Object> payload) {
        Object userInfo = userService.updateUserInfo(id,payload);

        DataResponse dataResponse = DataResponse.builder()
                .data(userInfo)
                .message("Change user information successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PatchMapping("/lock/{id}")
    public ResponseEntity<?> lockAccount(@PathVariable Integer id) {
        Account account = userService.lockAccount(id);

        DataResponse dataResponse = DataResponse.builder()
                .message("Lock account successfully")
                .success(true)
                .data(account)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);

        DataResponse dataResponse = DataResponse.builder()
                .message("Delete user successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }



}
