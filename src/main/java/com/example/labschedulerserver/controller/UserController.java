package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.payload.request.GetUserRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.payload.response.UserInfoResponse;
import com.example.labschedulerserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getUserInfo(@RequestBody GetUserRequest getUserRequest) {
        UserInfoResponse userInfoResponse = userService.getUserInfo(getUserRequest);

        DataResponse<Object> response = DataResponse.builder()
                .data(userInfoResponse)
                .success(true)
                .message("Get user information successfully")
                .build();

        return ResponseEntity.ok(response);
    }

}
