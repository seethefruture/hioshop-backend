package com.example.po;

import lombok.Data;

@Data
public class GoodsGallery {
    private String id;          // 图片ID
    private String goodsId;     // 商品ID
    private String imgUrl;      // 图片URL
    private String imgDesc;     // 图片描述
    private int sortOrder;      // 排序
    private boolean isDelete;   // 是否删除

    // 默认构造函数
    public GoodsGallery() {}

    // 参数化构造函数
    public GoodsGallery(String id, String goodsId, String imgUrl, String imgDesc, int sortOrder, boolean isDelete) {
        this.id = id;
        this.goodsId = goodsId;
        this.imgUrl = imgUrl;
        this.imgDesc = imgDesc;
        this.sortOrder = sortOrder;
        this.isDelete = isDelete;
    }
}
