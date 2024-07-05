package com.example.api;

import cn.hutool.core.util.StrUtil;
import com.example.po.UserPO;
import com.example.service.JwtService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> payload) {
        String token = jwtService.create(payload); // Assuming payload contains user info
        return ResponseEntity.ok(token);
    }

    @GetMapping("/info")
    public ResponseEntity<?> userInfo(@RequestHeader("Authorization") String token) {
        String userId = jwtService.getUserId(token);
        if (StrUtil.isEmpty(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        UserPO user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Implement logout logic if needed
        return ResponseEntity.ok("Logged out");
    }
}
