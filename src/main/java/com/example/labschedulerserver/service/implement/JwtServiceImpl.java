package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY = "aaa717f49d21b3dd550c599f9292916ef291a2387a37d56217810c56dcd468876a527dedc2ccc6ffdf7bb1c232492dae2a9cc678e73bed77014296a8321bffca65bc4b7deee4e4fdf1e980cea3ea318f80a191783a2924de025cdbdf9cf8d149e0bf1ffb19eb7b7cb7b7697b953fdb74b8f46ff09710f983f521f3c4f3a81147dc7dce9731fa2db1239d73f89d5735338bfd4501e0299e2d5d3b83e8268b9656b1c8fc73c2ae5e8abee10d2a32f703f5695361913afeeabd6c7bff2efa6dd9ba6c706ce342c41d13e82f0e0920d7c123b9613748a7c003a1d8c7783e273786d7722c06e80b52d136c1d94a97e60cebbbc981ce41cf723c5bc1ff3c3bfcb3fcdf";

    private Key getSignKey(){
        byte[] secretKeyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token).getBody();
    }

    @Override
    public List<String> getAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("authorities", List.class);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> function) {
        Claims claims = extractAllClaims(token);
        return function.apply(claims);
    }

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String extractId(String token) {
        return extractClaim(token, Claims::getId);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of("authorities",userDetails.getAuthorities().stream().map(String::valueOf).collect(Collectors.toList())),(Account) userDetails);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, Account userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setId(userDetails.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Duration.ofMinutes(30).toMillis()))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return new Date().after(expiration);
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
