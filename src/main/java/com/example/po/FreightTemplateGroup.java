package com.example.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FreightTemplateGroup {

    private String id; // 主键ID
    private String templateId; // 模板ID
    private String isDefault; // 是否默认，area:0
    private String area; // 区域设置（0位默认）
    private String start; // 起始数量
    private BigDecimal startFee; // 起始费用
    private String add; // 增加数量
    private BigDecimal addFee; // 增加费用
    private String freeByNumber; // 是否按数量免费，0没有设置
    private BigDecimal freeByMoney; // 是否按金额免费，0没设置
    private String isDelete; // 是否删除
}
