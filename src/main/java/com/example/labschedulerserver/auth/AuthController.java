package com.example.labschedulerserver.auth;

import com.example.labschedulerserver.payload.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.authenticate(authRequest);
        DataResponse response =  DataResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successfully")
                .data(authResponse)
                .build();
        return ResponseEntity.ok(response);
    }
}
