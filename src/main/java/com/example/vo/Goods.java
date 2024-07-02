package com.example.vo;

import lombok.Data;

import java.util.List;


@Data
public class Goods {
    private String id;
    private String name;
    private String goodsBrief;
    private Double minRetailPrice;
    private String listPicUrl;
    private Integer goodsNumber; // .
    private Boolean isOnSale;
    private Boolean isIndex;
    private Integer isDelete;
    private String categoryId;
    private double retailPrice;
    private Long goodsId;
    private String imgUrl;
    private String categoryName;
    private Boolean isNew;
    private List<Product> products;
    private String parentId;
}
