package com.example.po;

import lombok.Data;

@Data
public class Ad {
    private String id;  // 主键ID
    private Integer linkType;  // 链接类型（0: 商品，1: 链接）
    private String link;  // 链接地址
    private String goodsId;  // 商品ID
    private String imageUrl;  // 图片URL
    private Integer endTime;  // 结束时间
    private Integer enabled;  // 是否启用
    private Integer sortOrder;  // 排序
    private Integer isDelete;  // 是否删除
}
