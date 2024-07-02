package com.example.vo;

import lombok.Data;

@Data
public class Notice {
    private String id;
    private Boolean isDelete;
    private Long endTime;
    private String content;
    // Getters and Setters
}
