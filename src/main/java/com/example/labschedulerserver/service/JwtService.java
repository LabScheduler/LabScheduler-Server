package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Account;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    public Claims extractAllClaims(String token);

    public List<String> getAuthorities(String token);

    public <T> T extractClaim(String token, Function<Claims, T> function);

    public String extractUserName(String token);

    public String extractId(String token);

    public String generateToken(UserDetails userDetails);

    public String generateToken(Map<String, Object> extraClaims, Account userDetails);

    public boolean isTokenExpired(String token);

    public boolean validateToken(String token, UserDetails userDetails);
}
