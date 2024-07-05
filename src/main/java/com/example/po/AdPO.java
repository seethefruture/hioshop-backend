package com.example.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPO {
    private String id;  // 主键ID
    private Integer linkType;  // 链接类型（0: 商品，1: 链接）
    private String link;  // 链接地址
    private String goodsId;  // 商品ID
    private String imageUrl;  // 图片URL
    private Long endTime;  // 结束时间
    private Boolean enabled;  // 是否启用
    private Integer sortOrder;  // 排序
    private Boolean isDelete;  // 是否删除
}
