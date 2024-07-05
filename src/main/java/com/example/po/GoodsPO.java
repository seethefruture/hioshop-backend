package com.example.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GoodsPO {
    private String id; // 商品ID
    private String categoryId; // 分类ID
    private Boolean isOnSale; // 是否上架
    private String name; // 商品名称
    private int goodsNumber; // 商品数量
    private int sellVolume; // 销售量
    private String keywords; // 商品关键词
    private BigDecimal retailPrice; // 零售价格
    private BigDecimal minRetailPrice; // 最低零售价格
    private BigDecimal costPrice; // 成本价格
    private BigDecimal minCostPrice; // 最低成本价格
    private String goodsBrief; // 商品简介
    private String goodsDesc; // 商品描述
    private Integer sortOrder; // 排序
    private Boolean isIndex; // 是否首页展示
    private Boolean isNew; // 是否新品
    private String goodsUnit; // 商品单位
    private String httpsPicUrl; // 商品HTTPS图
    private String listPicUrl; // 商品列表图
    private String freightTemplateId; // 运费模板ID
    private Boolean freightType; // 运费类型
    private Boolean isDelete; // 是否删除
    private Boolean hasGallery; // 是否有图库
    private Boolean hasDone; // 是否已处理

    private List<GoodsGalleryPO> gallery; //@Transient
    private String CategoryName; //@Transient
    private List<ProductPO> products; //@Transient
}
