package com.example.po;

import lombok.Data;

@Data
public class Product {
    private Long id;
    private Long goodsId;
    private String goodsSn;
    private String goodsName;
    private double retailPrice;
    private int goodsNumber;
    private boolean isOnSale;
    private boolean isDeleted;
    private String goodsSpecificationIds;
    private Double goodsWeight;
    private int isDelete;
}
