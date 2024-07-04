package com.example.po;

import lombok.Data;

@Data
public class Notice {
    private String id;         // 通知ID
    private String content;    // 通知内容
    private int endTime;       // 结束时间（时间戳）
    private boolean isDelete;  // 是否删除（0: 否, 1: 是）
}
