package com.example.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductPO {
    private String id; // 产品ID
    private String goodsId; // 商品ID
    private String goodsSpecificationIds; // 商品规格ID
    private String goodsSn; // 商品编号
    private Integer goodsNumber; // 商品数量
    private Long retailPrice; // 零售价格
    private Long cost; // 成本
    private Long goodsWeight; // 重量
    private Boolean hasChange; // 是否可变
    private String goodsName; // 商品名称
    private Boolean isOnSale; // 是否上架
    private Boolean isDelete; // 是否删除

    private String value; // @Transient
}
