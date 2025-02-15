package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.mapper.UserMapper;
import com.example.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public String getLoginUserId() {
        // Implement your logic to get the logged-in user ID
        return "1"; // Placeholder implementation
    }

    public int updateUser(String userId, String name, String mobile, String nickname, String avatar, int nameMobile) {
        UserPO user = new UserPO();
        user.setId(userId);
        user.setName(name);
        user.setMobile(mobile);
        user.setNickname(nickname);
        user.setAvatar(avatar);
        user.setNameMobile(nameMobile);
        return userMapper.update(user);
    }

    public Map<String, Object> getUserDetail(String userId) {
        UserPO user = userMapper.findById(userId);
        if (user == null) {
            return null;
        }
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("id", user.getId());
        userDetails.put("mobile", user.getMobile());
        userDetails.put("name", user.getName());
        userDetails.put("nickname", user.getNickname());
        userDetails.put("avatar", user.getAvatar());
        return userDetails;
    }

    public UserPO getUserById(String userId) {
        return userMapper.findById(userId);
    }

    public void saveUser(UserPO user) {
        if (StrUtil.isNotEmpty(user.getId())) {
            userMapper.update(user);
        } else {
            user.setId(null);
            userMapper.insert(user);
        }
    }
}
