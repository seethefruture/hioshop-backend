package com.example.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressPO {
    private String id;             // 地址 ID
    private String name;             //
    private String userId;         // 用户 ID
    private String countryId;       // 国家 ID
    private String provinceId;      // 省份 ID
    private String cityId;          // 城市 ID
    private String districtId;      // 区域 ID
    private String address;        // 地址
    private String mobile;         // 手机号码
    private Boolean isDefault;         // 是否为默认地址 (0: 否, 1: 是)
    private Boolean isDelete;      // 是否删除 (0: 否, 1: 是)
}
