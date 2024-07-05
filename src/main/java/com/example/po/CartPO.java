package com.example.po;

import lombok.Data;


@Data
public class CartPO {
    private String id; // 主键ID
    private String userId; // 用户ID
    private String goodsId; // 商品ID
    private String goodsSn; // 商品编号
    private String productId; // 产品ID
    private String goodsName; // 商品名称
    private String goodsAka; // 商品别名
    private Double goodsWeight; // 商品重量
    private Double addPrice; // 加入购物车时的价格
    private Double retailPrice; // 零售价格
    private Integer number; // 数量
    private String goodsSpecifitionNameValue; // 规格属性组成的字符串
    private String goodsSpecifitionIds; // 商品规格ID
    private Boolean checked; // 是否选中
    private String listPicUrl; // 商品图片URL
    private String freightTemplateId; // 运费模板ID
    private Boolean isOnSale; // 是否上架
    private String addTime; // 添加时间
    private Integer isFast; // 是否快速购买
    private Integer isDelete; // 是否删除

}
