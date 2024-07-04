package com.example.po;

import lombok.Data;

@Data
public class User {
    private String id; // 用户ID
    private String nickname; // 用户昵称
    private String name; // 用户姓名
    private String username; // 用户名
    private String password; // 密码
    private int gender; // 性别（0-未知，1-男，2-女）
    private int birthday; // 生日（时间戳）
    private int registerTime; // 注册时间（时间戳）
    private int lastLoginTime; // 最后登录时间（时间戳）
    private String lastLoginIp; // 最后登录 IP
    private String mobile; // 手机号码
    private String registerIp; // 注册 IP
    private String avatar; // 头像 URL
    private String weixinOpenid; // 微信 OpenID
    private int nameMobile; // 是否显示手机号码（0-不显示，1-显示）
    private String country; // 国家
    private String province; // 省份
    private String city; // 城市
}
