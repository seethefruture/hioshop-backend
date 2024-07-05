package com.example.service;

import com.example.mapper.AdminMapper;
import com.example.mapper.ShipperMapper;
import com.example.mapper.ShowSettingsMapper;
import com.example.po.AdminPO;
import com.example.po.ShowSettingsPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private ShowSettingsMapper selectShowSettings;
    @Autowired
    private ShipperMapper shipperMapper;

    public List<AdminPO> getAllAdmins() {
        List<AdminPO> admins = adminMapper.selectAll();
        return admins;
    }

    public AdminPO getAdminById(String id) {
        return adminMapper.selectById(id);
    }

    public void addAdmin(AdminPO admin) {
        String passwordSalt = "HIOLABS";
        String password = DigestUtils.md5DigestAsHex((admin.getPassword() + passwordSalt).getBytes(StandardCharsets.UTF_8));
        admin.setPasswordSalt(passwordSalt);
        admin.setPassword(password);
        adminMapper.insert(admin);
    }

    public void saveAdmin(Map<String, Object> payload) {
        AdminPO admin = new AdminPO();
        String id = String.valueOf(payload.get("id"));
        String username = (String) payload.get("username");

        if ((Boolean) payload.get("change")) {
            String newPassword = (String) payload.get("newpassword");
            if (!newPassword.trim().isEmpty()) {
                newPassword = DigestUtils.md5DigestAsHex((newPassword + admin.getPasswordSalt()).getBytes(StandardCharsets.UTF_8));
                adminMapper.updateUsernameAndPassword(id, username, newPassword);
            }
        }

        AdminPO existingAdmin = adminMapper.selectByUsername(admin.getUsername());
        if (existingAdmin != null) {
            throw new RuntimeException("重名了");
        }
        adminMapper.updateUsername(id, username);
    }

    public void deleteAdmin(String id) {
        adminMapper.deleteById(id);
    }

    public ShowSettingsPO getShowSettings() {
        return selectShowSettings.selectShowSettings();
    }

    public void updateShowSettings(Map<String, Object> settings) {
        selectShowSettings.updateShowSettings(settings);
    }

    public void changeAutoStatus(Boolean status) {
        selectShowSettings.updateAutoStatus(status);
    }

    public void updateShipperSettings(ShowSettingsPO settings) {
        adminMapper.update(settings);
    }

    public Map<String, Object> getSenderInfo() {
        throw new RuntimeException("TODO");
//        return adminMapper.selectSenderInfo();
    }

    public AdminPO login(String username, String password, String ip) throws Exception {
        AdminPO admin = adminMapper.selectByUsername(username);
        if (admin == null) {
            throw new Exception("用户名或密码不正确!");
        }
        String passwordHash = DigestUtils.md5DigestAsHex((password + admin.getPasswordSalt()).getBytes(StandardCharsets.UTF_8));
        if (!passwordHash.equals(admin.getPassword())) {
            throw new Exception("用户名或密码不正确!!");
        }

        admin.setLastLoginTime(System.currentTimeMillis());
        admin.setLastLoginIp(ip);
        adminMapper.updateUsernameAndPassword(admin.getId(), admin.getUsername(), admin.getPassword());
        return admin;
    }
}
