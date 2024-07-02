package com.example.po;

import lombok.Data;

@Data
public class GoodsGallery {

    private Long id;
    private Long goodsId;
    private String imgUrl;
    private Integer isDelete;
}
