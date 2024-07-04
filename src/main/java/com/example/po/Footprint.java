package com.example.po;

import lombok.Data;

@Data
public class Footprint {
    private String id; // 记录ID
    private String userId; // 用户ID
    private String goodsId; // 商品ID
    private int addTime; // 添加时间
}
