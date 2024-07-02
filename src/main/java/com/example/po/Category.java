package com.example.po;

import lombok.Data;

import java.util.List;

@Data
public class Category {
    private Long id;
    private String name;
    private String imgUrl;
    private Integer pHeight;
    private Integer parentId;
    private Integer isCategory;
    private Integer sortOrder;
    private String iconUrl;
    private String banner;
    private Integer height;
    private List<Goods> goodsList;
    // getters and setters
}
