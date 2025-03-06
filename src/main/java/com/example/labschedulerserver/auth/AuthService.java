package com.example.labschedulerserver.auth;

import com.example.labschedulerserver.common.AccountStatus;
import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.model.Role;
import com.example.labschedulerserver.repository.RoleRepository;
import com.example.labschedulerserver.service.JwtService;
import com.example.labschedulerserver.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final RoleRepository roleRepository;

    public AuthResponse authenticate(AuthRequest authRequest) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        }catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("Invalid email or password");
        }
        Account user = userService.getUserByEmail(authRequest.getEmail()).get();

        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
