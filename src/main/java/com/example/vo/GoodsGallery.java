package com.example.vo;

import lombok.Data;

@Data
public class GoodsGallery {

    private String id;
    private String goodsId;
    private String imgUrl;
    private Boolean isDelete;
}
