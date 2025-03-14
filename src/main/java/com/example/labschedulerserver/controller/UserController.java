package com.example.labschedulerserver.controller;


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

    @PatchMapping("/{id}")
    public ResponseEntity<?> changeUserInfo(@PathVariable Integer id,@RequestBody Map<String,Object> payload) {
        Object userInfo = userService.changeUserInfo(id,payload);

        DataResponse dataResponse = DataResponse.builder()
                .data(userInfo)
                .message("Change user information successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }



}
