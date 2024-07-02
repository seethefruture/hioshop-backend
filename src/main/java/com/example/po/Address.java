package com.example.po;

import lombok.Data;

@Data
public class Address {

    private String id;
    private String name;
    private String mobile;
    private String provinceId;
    private String cityId;
    private String districtId;
    private String address;
    private String userId;
    private Integer isDefault; // 标识是用户默认收货地址，有且只有一个
    private Integer isDelete;
    private String provinceName;
    private String cityName;
    private String districtName;
    private String fullRegion;

}