package com.example.vo;

import lombok.Data;

@Data
public class Admin {

    private Long id;
    private String name;
    private String username;
    private String password;
    private String passwordSalt;
    private Long lastLoginTime;
    private String lastLoginIp;
    private String lastLoginTimeFormatted;
    private boolean isDelete;
}
