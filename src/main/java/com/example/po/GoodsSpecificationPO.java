package com.example.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 商品对应规格表值表
 */
public class GoodsSpecificationPO implements Serializable {

    private String id; // 主键ID
    private String goodsId; // 商品ID
    private String specificationId; // 规格ID
    private String value; // 规格值
    private String picUrl; // 图片URL
    private boolean isDelete; // 是否删除，0为未删除，1为删除
    private Integer goodsNumber; // @Transient

    public GoodsSpecificationPO(String goodsSpecificationId, String goodsId, String specValue, String specificationId, String s, boolean b) {
        this.id = goodsSpecificationId;
        this.goodsId = goodsId;
        this.value = specValue;
        this.specificationId = specificationId;
        this.picUrl = s;
        this.isDelete = b;
    }
}
