package com.example.po;

import lombok.Data;

import java.io.Serializable;
@Data
/**
 * 商品对应规格表值表
 */
public class GoodsSpecification implements Serializable {

    private String id; // 主键ID
    private String goodsId; // 商品ID
    private String specificationId; // 规格ID
    private String value; // 规格值
    private String picUrl; // 图片URL
    private boolean isDelete; // 是否删除，0为未删除，1为删除
}
