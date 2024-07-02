package com.example.service;

import com.example.mapper.AdminMapper;
import com.example.vo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public List<Admin> getAllAdmins() {
        List<Admin> admins = adminMapper.selectAll();
        admins.forEach(admin -> {
            if (admin.getLastLoginTime() != 0) {
                admin.setLastLoginTimeFormatted(String.valueOf(admin.getLastLoginTime()));
            } else {
                admin.setLastLoginTimeFormatted("还没登录过");
            }
            admin.setPassword("");
        });
        return admins;
    }

    public Admin getAdminById(Long id) {
        return adminMapper.selectById(id);
    }

    public void addAdmin(Admin admin) {
        String passwordSalt = "HIOLABS";
        String password = DigestUtils.md5DigestAsHex((admin.getPassword() + passwordSalt).getBytes(StandardCharsets.UTF_8));
        admin.setPasswordSalt(passwordSalt);
        admin.setPassword(password);
        adminMapper.insert(admin);
    }

    public void saveAdmin(Map<String, Object> payload) {
        Admin admin = new Admin();
        String id = String.valueOf(payload.get("id"));
        String username = (String) payload.get("username");

        if ((Boolean) payload.get("change")) {
            String newPassword = (String) payload.get("newpassword");
            if (!newPassword.trim().isEmpty()) {
                newPassword = DigestUtils.md5DigestAsHex((newPassword + admin.getPasswordSalt()).getBytes(StandardCharsets.UTF_8));
                adminMapper.updateUsernameAndPassword(id, username, newPassword);
            }
        }

        Admin existingAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (existingAdmin != null) {
            throw new RuntimeException("重名了");
        }

        adminMapper.updateUsername(id, username);
    }

    public void deleteAdmin(Long id) {
        adminMapper.deleteById(id);
    }

    public Map<String, Object> getShowSettings() {
        return adminMapper.selectShowSettings();
    }

    public void updateShowSettings(Map<String, Object> settings) {
        adminMapper.updateShowSettings(settings);
    }

    public void changeAutoStatus(Boolean status) {
        adminMapper.updateAutoStatus(status);
    }

    public void updateShipperSettings(Map<String, Object> settings) {
        adminMapper.updateShipperSettings(settings);
    }

    public Map<String, Object> getSenderInfo() {
        return adminMapper.selectSenderInfo();
    }

    public Admin login(String username, String password, String ip) throws Exception {
        Admin admin = adminMapper.selectByUsername(username);
        if (admin == null) {
            throw new Exception("用户名或密码不正确!");
        }
        String passwordHash = DigestUtils.md5DigestAsHex((password + admin.getPasswordSalt()).getBytes(StandardCharsets.UTF_8));
        if (!passwordHash.equals(admin.getPassword())) {
            throw new Exception("用户名或密码不正确!!");
        }

        admin.setLastLoginTime(System.currentTimeMillis() / 1000);
        admin.setLastLoginIp(ip);
        adminMapper.updateUsernameAndPassword(admin);
        return admin;
    }
}
