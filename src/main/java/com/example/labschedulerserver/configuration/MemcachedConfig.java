package com.example.labschedulerserver.configuration;

import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;

@Configuration
public class MemcachedConfig {

    @Value("${spring.memcached.host}")
    private String memcachedHost;

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        return new MemcachedClient(new InetSocketAddress(memcachedHost, 11211));
    }
}
