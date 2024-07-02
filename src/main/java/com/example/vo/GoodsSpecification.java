package com.example.vo;

import lombok.Data;

@Data
public class GoodsSpecification {
    private String id;
    private String goodsId;
    private String specificationId;
    private Boolean isDelete;
    private int goodsNumber;
    private String value;
    private String name;
    // Getters and setters
}
