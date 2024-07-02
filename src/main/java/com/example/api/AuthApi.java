package com.example.api;

import cn.hutool.core.util.StrUtil;
import com.example.service.WeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthApi {

    @Autowired
    private WeixinService weixinService;

    /**
     * 微信小程序登录
     *
     * @param payload .
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginByWeixin(@RequestBody Map<String, String> payload) throws Exception {
        String code = payload.get("code");
        if (StrUtil.isEmpty(code)) {
            return ResponseEntity.badRequest().body("Code is required");
        }
        Map<String, Object> response = weixinService.loginByWeixin(code);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logout successful");
    }
}
