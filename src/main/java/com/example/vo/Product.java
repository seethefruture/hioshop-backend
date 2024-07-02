package com.example.vo;

import lombok.Data;

@Data
public class Product {
    private String id;
    private String goodsId;
    private String goodsSn;
    private String goodsName;
    private double retailPrice;
    private int goodsNumber;
    private boolean isOnSale;
    private boolean isDeleted;
    private String goodsSpecificationIds;
    private Double goodsWeight;
    private Boolean isDelete;
    private String value;
}
