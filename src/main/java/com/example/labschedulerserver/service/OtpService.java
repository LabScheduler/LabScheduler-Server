package com.example.labschedulerserver.service;

public interface OtpService {
    String generateOtp(String us);

    boolean validateOtp(String username, String otp);

    boolean removeOtpFromCache(String username);
}
