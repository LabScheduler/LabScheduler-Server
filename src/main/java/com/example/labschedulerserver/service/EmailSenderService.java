package com.example.labschedulerserver.service;

public interface EmailSenderService {
    public void sendOtp(String email, String otp);
}
