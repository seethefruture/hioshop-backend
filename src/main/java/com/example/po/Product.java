package com.example.po;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class Product {
    private String id; // 产品ID
    private String goodsId; // 商品ID
    private String goodsSpecificationIds; // 商品规格ID
    private String goodsSn; // 商品编号
    private String goodsNumber; // 商品数量
    private BigDecimal retailPrice; // 零售价格
    private BigDecimal cost; // 成本
    private double goodsWeight; // 重量
    private boolean hasChange; // 是否可变
    private String goodsName; // 商品名称
    private boolean isOnSale; // 是否上架
    private boolean isDelete; // 是否删除
}
