package com.example.labschedulerserver.configuration;

import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.Account;
import com.example.labschedulerserver.service.JwtService;
import com.example.labschedulerserver.service.RoleService;
import com.example.labschedulerserver.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final RoleService roleService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String username;

        if(request.getRequestURI().contains("/api/auth") && request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\":\"Bro need to authorize\"}");
            return;
        }

        try{
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);

            if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null) {
                boolean user = userService.checkUserIfExist(username);
                if(!user){
                    throw new ResourceNotFoundException("User not found");
                }

                UserDetails userDetails = Account.builder()
                        .id(Long.valueOf(jwtService.extractId(token)))
                        .email(jwtService.extractUserName(token))
                        .role(roleService.findRoleByName(jwtService.getAuthorities(token).get(0)))
                        .build();

                if(jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\":\"Unauthorized\"}");
        }

    }
}
