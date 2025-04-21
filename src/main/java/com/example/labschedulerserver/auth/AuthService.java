package com.example.labschedulerserver.auth;

import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.exception.UnauthorizedException;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.response.User.UserMapper;
import com.example.labschedulerserver.repository.RoleRepository;
import com.example.labschedulerserver.service.JwtService;
import com.example.labschedulerserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        }catch (Exception e){
            throw new UnauthorizedException("Invalid username or password");
        }
        Account user = userService.findByUsername(authRequest.getUsername());

        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .userInfo(UserMapper.mapUserToResponse(user,userService.getUserInfo(user.getId())))
                .build();
    }

}
