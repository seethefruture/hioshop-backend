package com.example.vo;

import lombok.Data;

@Data
public class OrderGoods {
    private String id;
    private String orderId;
    private String goodsId;
    private String productId;
    private Integer number;
    private String listPicUrl;
    private Double retailPrice;
    private String goodsSpecifitionNameValue;
    // Getters and Setters
}
