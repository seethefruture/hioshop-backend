package com.example.api;

import com.example.po.ShowSettingsPO;
import com.example.po.AdminPO;
import com.example.po.UserPO;
import com.example.service.AdminService;
import com.example.service.JwtService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/admins")
    public ResponseEntity<?> index() {
        List<AdminPO> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @PostMapping("/admin/detail")
    public ResponseEntity<?> adminDetail(@RequestBody Map<String, String> payload) {
        String id = payload.get("id");
        AdminPO admin = adminService.getAdminById(id);
        return ResponseEntity.ok(admin);
    }

    @PostMapping("/admin/add")
    public ResponseEntity<?> adminAdd(@RequestBody AdminPO admin) {
        adminService.addAdmin(admin);
        return ResponseEntity.ok("AdminPO added successfully");
    }

    @PostMapping("/admin/save")
    public ResponseEntity<?> adminSave(@RequestBody Map<String, Object> payload) {
        adminService.saveAdmin(payload);
        return ResponseEntity.ok("AdminPO saved successfully");
    }

    @GetMapping("/user/info")
    public ResponseEntity<?> userInfo(@RequestParam String id) {
        UserPO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/user/store")
    public ResponseEntity<?> storeUser(@RequestBody UserPO user) {
        userService.saveUser(user);
        return ResponseEntity.ok("UserPO saved successfully");
    }

    @PostMapping("/admin/delete")
    public ResponseEntity<?> deleteAdmin(@RequestBody Map<String, String> payload) {
        String id = payload.get("id");
        adminService.deleteAdmin(id);
        return ResponseEntity.ok("AdminPO deleted successfully");
    }

    @GetMapping("/showset")
    public ResponseEntity<?> showSet() {
        ShowSettingsPO settings = adminService.getShowSettings();
        return ResponseEntity.ok(settings);
    }

    @PostMapping("/showset/store")
    public ResponseEntity<?> showSetStore(@RequestBody Map<String, Object> settings) {
        adminService.updateShowSettings(settings);
        return ResponseEntity.ok("Settings updated successfully");
    }

    @PostMapping("/settings/auto")
    public ResponseEntity<?> changeAutoStatus(@RequestBody Map<String, String> payload) {
        Boolean status = Boolean.parseBoolean(payload.get("status"));
        adminService.changeAutoStatus(status);
        return ResponseEntity.ok("Auto status updated successfully");
    }

    @PostMapping("/settings/shipper")
    public ResponseEntity<?> storeShipperSettings(@RequestBody ShowSettingsPO settings) {
        adminService.updateShipperSettings(settings);
        return ResponseEntity.ok("Shipper settings updated successfully");
    }

    @GetMapping("/sender/info")
    public ResponseEntity<?> senderInfo() {
        Map<String, Object> info = adminService.getSenderInfo();
        return ResponseEntity.ok(info);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String username = payload.get("username");
        String password = payload.get("password");

        try {
            AdminPO admin = adminService.login(username, password, request.getRemoteAddr());
            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put("user_id", admin.getId());
            String sessionKey = jwtService.create(sessionData);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", admin.getId());
            userInfo.put("username", admin.getUsername());
//            userInfo.put("name", admin.getName());

            Map<String, Object> response = new HashMap<>();
            response.put("token", sessionKey);
            response.put("userInfo", userInfo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
