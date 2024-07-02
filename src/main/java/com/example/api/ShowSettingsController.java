package com.example.api;

import com.example.service.ShowSettingsService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ShowSettingsController {

    @Autowired
    private ShowSettingsService showSettingsService;

    @Autowired
    private UserService userService;

    @GetMapping("/showSettings")
    public ResponseEntity<?> showSettings() {
        Map<String, Object> info = showSettingsService.getShowSettings();
        return ResponseEntity.ok(info);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Map<String, String> payload) {
        String userId = userService.getLoginUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        String name = payload.get("name");
        String mobile = payload.get("mobile");
        String nickName = payload.get("nickName");
        String avatar = payload.get("avatar");
        int nameMobile = (name != null && !name.isEmpty() && mobile != null && !mobile.isEmpty()) ? 1 : 0;

        String nickname = Base64.getEncoder().encodeToString(nickName.getBytes(StandardCharsets.UTF_8));
        int info = userService.updateUser(userId, name, mobile, nickname, avatar, nameMobile);
        return ResponseEntity.ok(info);
    }

    @GetMapping("/userDetail")
    public ResponseEntity<?> userDetail() {
        String userId = userService.getLoginUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        Map<String, Object> info = userService.getUserDetail(userId);
        if (info != null) {
            String nickname = new String(Base64.getDecoder().decode((String) info.get("nickname")), StandardCharsets.UTF_8);
            info.put("nickname", nickname);
            return ResponseEntity.ok(info);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve user details");
        }
    }
}
