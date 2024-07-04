package com.example.po;

import lombok.Data;

@Data
public class Admin {

    private String id; // 管理员ID
    private String username; // 用户名
    private String password; // 密码
    private String passwordSalt; // 密码盐
    private String lastLoginIp; // 最后登录IP
    private long lastLoginTime; // 最后登录时间（Unix时间戳）
    private boolean isDelete; // 是否已删除（0表示未删除，1表示已删除）

}
