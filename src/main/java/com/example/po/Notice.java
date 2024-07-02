package com.example.po;

import lombok.Data;

@Data
public class Notice {
    private int id;
    private int isDelete;
    private long endTime;
    private String content;
    // Getters and Setters
}
