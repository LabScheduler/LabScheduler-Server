package com.example.labschedulerserver.auth;

import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.forgotPassword(email))
                .message("Send email successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.verifyOtp(email, otp))
                .message("Verify OTP successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String otp, @RequestParam(name = "new_password") String newPassword) {
        DataResponse dataResponse = DataResponse.builder()
                .data(userService.resetPassword(email, otp, newPassword))
                .message("Reset password successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }
}
