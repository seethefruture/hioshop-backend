package com.example.po;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Footprint {

    private String id;
    private String userId;
    private String goodsId;
    private String addTime;
    private Goods goods;

    public Footprint(String userId, String goodsId) {
        this.userId = userId;
        this.goodsId = goodsId;
    }
}
