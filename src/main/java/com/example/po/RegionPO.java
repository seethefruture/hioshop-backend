package com.example.po;

import lombok.Data;

@Data
public class RegionPO {
    private String id; // 主键ID
    private String parentId; // 父级区域ID
    private String name; // 区域名称
    private byte type; // 区域类型
    private String agencyId; // 代理商ID
    private String area; // 方位，根据这个定运费
    private String areaCode; // 方位代码
    private String farArea; // 偏远地区
}
