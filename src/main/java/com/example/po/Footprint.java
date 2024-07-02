package com.example.po;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Footprint {

    private Long id;
    private Long userId;
    private Long goodsId;
    private String addTime;
    private Goods goods;

    public Footprint(Long userId, Long goodsId) {
        this.userId = userId;
        this.goodsId = goodsId;
    }
}
