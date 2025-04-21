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
    public String generateOtp(String username) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        memcachedClient.set(username,(int) TimeUnit.MINUTES.toSeconds(5), otp);
        return otp;
    }

    @Override
    public boolean validateOtp(String username, String otp) {
        Object cachedOtp = memcachedClient.get(username);
        //            memcachedClient.delete(email);
        return cachedOtp != null && cachedOtp.equals(otp);
    }

    @Override
    public boolean removeOtpFromCache(String email) {
        memcachedClient.delete(email);
        return true;
    }
}
