package com.example.po;

import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
