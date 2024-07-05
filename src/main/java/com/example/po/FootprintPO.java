package com.example.po;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FootprintPO {
    private String id; // 记录ID
    private String userId; // 用户ID
    private String goodsId; // 商品ID
    private Long addTime; // 添加时间


    private GoodsPO goods; //@Transient

    public FootprintPO(String id, String userId, String goodsId) {
        this.userId = userId;
        this.goodsId = goodsId;
        this.id = id;
        this.addTime = System.currentTimeMillis();
    }
}
