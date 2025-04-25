package com.example.labschedulerserver.auth;

import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.exception.UnauthorizedException;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.response.User.UserMapper;
import com.example.labschedulerserver.repository.AccountRepository;
import com.example.labschedulerserver.repository.RoleRepository;
import com.example.labschedulerserver.service.EmailSenderService;
import com.example.labschedulerserver.service.JwtService;
import com.example.labschedulerserver.service.OtpService;
import com.example.labschedulerserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final EmailSenderService emailSenderService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticate(AuthRequest authRequest) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        }catch (Exception e){
            throw new UnauthorizedException("Invalid username or password");
        }
        Account user = userService.findByUsername(authRequest.getUsername());

        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }

    public String forgotPassword(String username) {
        Account user = userService.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        String otp = otpService.generateOtp(username);
        String email = accountRepository.findEmailByAccountId(user.getId()).orElseThrow(()->new ResourceNotFoundException("User not found"));
        emailSenderService.sendOtp(email, otp);

        String[] parts = email.split("@");
        String local = parts[0];
        String domain = parts[1];

        String masked = local.charAt(0) + "****" + local.charAt(local.length() - 1);

        return masked + "@" + domain;
    }
    public boolean verifyOtp(String username, String otp) {
        if (otpService.validateOtp(username, otp)) {
            otpService.removeOtpFromCache(username);
            return true;
        } else {
            throw new UnauthorizedException("Invalid OTP");
        }
    }
    public boolean resetPassword(String username, String newPassword) {
        Account account = userService.findByUsername(username);
        if (account == null) {
            throw new ResourceNotFoundException("User not found");
        }
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        return true;
    }

}
