package com.example.po;

import lombok.Data;

@Data
public class OrderGoods {
    private Long id;
    private Long orderId;
    private Long goodsId;
    private Long productId;
    private Integer number;
    private String listPicUrl;
    private Double retailPrice;
    private String goodsSpecifitionNameValue;
    // Getters and Setters
}
