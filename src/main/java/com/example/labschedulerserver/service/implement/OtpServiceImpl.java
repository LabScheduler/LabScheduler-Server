package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.service.OtpService;
import lombok.RequiredArgsConstructor;
import net.spy.memcached.MemcachedClient;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final MemcachedClient memcachedClient;

    @Override
    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        memcachedClient.set(email,(int) TimeUnit.MINUTES.toSeconds(5), otp);
        return otp;
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        Object cachedOtp = memcachedClient.get(email);
        if (cachedOtp != null && cachedOtp.equals(otp)) {
            memcachedClient.delete(email);
            return true;
        }
        return false;
    }
}
