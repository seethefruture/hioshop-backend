package com.example.service;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public Long getUserId(String token) {
        // 根据token获取用户ID的逻辑
        return 1L; // 示例，实际应从token解析用户ID
    }
}
