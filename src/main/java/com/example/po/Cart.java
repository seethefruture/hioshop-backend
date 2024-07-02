package com.example.po;

import lombok.Data;

@Data
public class Cart {
    private Long id;
    private Long goodsId;
    private Long productId;
    private String goodsSn;
    private String goodsName;
    private String listPicUrl;
    private int number;
    private Long userId;
    private double retailPrice;
    private double addPrice;
    private String goodsSpecificationNameValue;
    private boolean checked; // 是否选中
    private long addTime;
    private Integer isDelete;
    private Integer isFast; // 猜测是加入购物车和立即购买
    private Integer goodsNumber;
    private Double goodsWeight;
    private Double weightCount;
    // Getters and setters

}
