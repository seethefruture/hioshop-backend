package com.example.po;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderGoods {

    private String id; // 主键ID
    private String orderId; // 订单ID
    private String goodsId; // 商品ID
    private String goodsName; // 商品名称
    private String goodsAka; // 商品别名
    private String productId; // 产品ID
    private Integer number; // 商品数量
    private BigDecimal retailPrice; // 商品零售价格
    private String goodsSpecifitionNameValue; // 商品规格名称及其值
    private String goodsSpecifitionIds; // 商品规格ID
    private String listPicUrl; // 商品图片URL
    private String userId; // 用户ID
    private Boolean isDelete; // 是否删除标志
}
