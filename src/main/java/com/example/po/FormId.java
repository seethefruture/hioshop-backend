package com.example.po;

import lombok.Data;

@Data
public class FormId {
    private String id;          // 主键ID
    private String userId;      // 用户ID
    private String orderId;     // 订单ID
    private String formId;      // 表单ID
    private int addTime;        // 添加时间
    private byte useTimes;      // 使用次数
}
