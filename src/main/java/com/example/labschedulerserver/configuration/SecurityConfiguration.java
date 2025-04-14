package com.example.labschedulerserver.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    String [] whiteListForAll = {
            "/api/auth/**",
    };

    String [] whiteListForAllRole = {
            // Basic viewing endpoints
            "/api/course",
            "/api/course/{id}",
            "/api/class",
            "/api/class/{id}",
            "/api/subject",
            "/api/subject/{id}",
            "/api/room",
            "/api/room/{id}",
            "/api/semester",
            "/api/semester/current",
            "/api/semester/weeks",
            "/api/department",
            "/api/department/{id}",
            "/api/major",
            "/api/major/{id}",
            "/api/lecturer",
            
            // User profile related
            "/api/user/{id}",
            "/api/user/change-password",
            "/api/user/update/{id}",
            
            // Schedule viewing
            "/api/schedule",
            "/api/schedule/semester/{semesterId}",
            "/api/schedule/class/{classId}",
            "/api/schedule/course/{courseId}",
            "/api/schedule/lecturer/{lecturerId}",
            "/api/schedule/week/{weekId}",
            "/api/schedule/filter",
            
            // Dashboard summary
            "/api/sumary/dashboard"
    };

    String [] whiteListForLecturerRole = {
            // Lecturer specific endpoints
            "/api/lecturer-request/create",
            "/api/lecturer-request/get/lecturer/{lecturerId}",
            "/api/lecturer-request/get/{requestId}",
            "/api/lecturer-request/cancel",
            "/api/course/section/{courseId}",
    };

    String [] whiteListForManagerRole = {
            // Management operations
            "/api/user/**",
            "/api/class/**",
            "/api/course/**",
            "/api/subject/**",
            "/api/room/**",
            "/api/department/**",
            "/api/major/**",
            "/api/schedule/**",
            "/api/lecturer-request/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(whiteListForAll).permitAll()
                        .requestMatchers(whiteListForAllRole).hasAnyAuthority("STUDENT", "LECTURER", "MANAGER")
                        .requestMatchers(whiteListForLecturerRole).hasAnyAuthority("LECTURER", "MANAGER")
                        .requestMatchers(whiteListForManagerRole).hasAuthority("MANAGER")
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
