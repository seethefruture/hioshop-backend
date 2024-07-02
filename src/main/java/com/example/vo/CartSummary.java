package com.example.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CartSummary {
    private List<Cart> cartList;
    private int goodsCount;
    private BigDecimal goodsAmount;
    private int checkedGoodsCount;
    private BigDecimal checkedGoodsAmount;
    private Long userId;
    private boolean numberChange;
}
