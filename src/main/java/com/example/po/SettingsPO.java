package com.example.po;

import lombok.Data;

@Data
public class SettingsPO {
    private String id; // 主键ID
    private boolean autoDelivery; // 是否自动配送
    private String name; // 名称
    private String tel; // 联系电话
    private String provinceName; // 省份名称
    private String cityName; // 城市名称
    private String expAreaName; // 扩展区域名称
    private String address; // 详细地址
    private int discoveryImgHeight; // 发现图片高度
    private String discoveryImg; // 发现图片链接
    private String goodsId; // 商品ID
    private String cityId; // 城市ID
    private String provinceId; // 省份ID
    private String districtId; // 区县ID
    private Long countdown; // 10分钟倒计时
    private Boolean reset; // 是否重置
}
