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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.authenticate(authRequest);
        DataResponse dataResponse = DataResponse.builder()
                .data(authResponse)
                .message("Login successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String username) {
        DataResponse dataResponse = DataResponse.builder()
                .data(authService.forgotPassword(username))
                .message("Send email successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestParam String username, @RequestParam String otp) {
        DataResponse dataResponse = DataResponse.builder()
                .data(authService.verifyOtp(username, otp))
                .message("Verify OTP successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String username, @RequestParam String newPassword) {
        DataResponse dataResponse = DataResponse.builder()
                .data(authService.resetPassword(username, newPassword))
                .message("Reset password successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(dataResponse);
    }
}
