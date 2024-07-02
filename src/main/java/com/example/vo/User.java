package com.example.vo;

import lombok.Data;

@Data
public class User {

    private String id;
    private String username;
    private String password;
    private Long registerTime;
    private String registerIp;
    private Long lastLoginTime;
    private String lastLoginIp;
    private String weixinOpenid;
    private String nickname;
    private String avatar;
    private String name;
    private String mobile;
    private int nameMobile;
    private String ruleContent;
    private String passwordSalt;
    private boolean isShow;
    private boolean isNew;
}
