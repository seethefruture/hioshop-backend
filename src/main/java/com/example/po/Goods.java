package com.example.po;

import lombok.Data;


@Data
public class Goods {
    private Long id;
    private String name;
    private String goodsBrief;
    private Double minRetailPrice;
    private String listPicUrl;
    private Integer goodsNumber; // .
    private Boolean isOnSale;
    private Integer isDelete;
    private Long categoryId;
    private double retailPrice;
    private Long goodsId;
    private String imgUrl;
    private Boolean isNew;
}
