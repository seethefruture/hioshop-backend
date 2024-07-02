package com.example.po;

import lombok.Data;

@Data
public class Cart {
    private String id;
    private String goodsId;
    private String productId;
    private String goodsSn;
    private String goodsName;
    private String listPicUrl;
    private int number;
    private String userId;
    private double retailPrice;
    private double addPrice;
    private String goodsSpecificationNameValue;
    private boolean checked; // 是否选中
    private Long addTime;
    private Integer isDelete;
    private Integer isFast; // 猜测是加入购物车和立即购买
    private Integer goodsNumber;
    private Double goodsWeight;
    private Double weightCount;
    // Getters and setters

}
