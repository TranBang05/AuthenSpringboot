package com.example.demo_mhdigital.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisConnectionService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void checkRedisConnection() {
        try {
            // Thực hiện một hoạt động đơn giản để kiểm tra kết nối
            String key = "testKey";
            String value = "testValue";
            redisTemplate.opsForValue().set(key, value);

            // Lấy giá trị từ Redis
            String retrievedValue = redisTemplate.opsForValue().get(key);

            // In ra console để kiểm tra
            System.out.println("Connected to Redis. Retrieved value: " + retrievedValue);
        } catch (Exception e) {
            // Xử lý nếu có lỗi kết nối
            System.err.println("Failed to connect to Redis: " + e.getMessage());
        }
    }
}
