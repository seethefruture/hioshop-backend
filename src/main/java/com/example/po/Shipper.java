package com.example.po;

import lombok.Data;

import java.io.Serializable;
@Data
public class Shipper {
    private String id;  // 唯一标识
    private String name;  // 快递公司名称
    private String code;  // 快递公司代码
    private Integer sortOrder;  // 排序
    private String monthCode;  // 月码
    private String customerName;  // 客户名称
    private Boolean enabled;  // 是否启用
}
