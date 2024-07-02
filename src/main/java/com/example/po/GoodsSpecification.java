package com.example.po;

import lombok.Data;

@Data
public class GoodsSpecification {
    private String id;
    private String goodsId;
    private String specificationId;
    private Boolean isDelete;
    private int goodsNumber;

    // Getters and setters
}
