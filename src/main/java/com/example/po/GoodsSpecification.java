package com.example.po;

import lombok.Data;

@Data
public class GoodsSpecification {
    private Long id;
    private Long goodsId;
    private Long specificationId;
    private int isDelete;
    private int goodsNumber;

    // Getters and setters
}
