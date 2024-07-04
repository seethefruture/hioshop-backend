package com.example.po;

import lombok.Data;

@Data
public class Address {
    private String id;             // 地址 ID
    private String userId;         // 用户 ID
    private short countryId;       // 国家 ID
    private short provinceId;      // 省份 ID
    private short cityId;          // 城市 ID
    private short districtId;      // 区域 ID
    private String address;        // 地址
    private String mobile;         // 手机号码
    private int isDefault;         // 是否为默认地址 (0: 否, 1: 是)
    private boolean isDelete;      // 是否删除 (0: 否, 1: 是)
}
