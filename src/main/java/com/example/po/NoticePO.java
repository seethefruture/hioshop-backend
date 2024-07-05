package com.example.po;

import lombok.Data;

@Data
public class NoticePO {
    private String id;         // 通知ID
    private String content;    // 通知内容
    private Long endTime;       // 结束时间（时间戳）
    private Boolean isDelete;  // 是否删除（0: 否, 1: 是）
}
