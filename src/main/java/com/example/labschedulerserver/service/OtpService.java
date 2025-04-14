package com.example.labschedulerserver.service;

public interface OtpService {
    String generateOtp(String email);

    boolean validateOtp(String email, String otp);

    boolean removeOtpFromCache(String email);
}
