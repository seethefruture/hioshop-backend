package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.example.mapper.AddressMapper;
import com.example.mapper.CartMapper;
import com.example.mapper.GoodsMapper;
import com.example.mapper.ProductMapper;
import com.example.po.AddressPO;
import com.example.po.CartPO;
import com.example.po.GoodsPO;
import com.example.po.ProductPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    public List<CartPO> getGoodsList(String userId) {
        return cartMapper.getCharts(userId, null, null);
    }

    public List<CartPO> getCheckedGoodsList(String userId) {
        return cartMapper.getCharts(userId, null, null);
    }

    public int clearBuyGoods(String userId) {
        return cartMapper.deleteCheckedProducts(userId);
    }

    public Map<String, Object> getCarts(int type) {
        String userId = getLoginUserId();
        List<CartPO> cartList = (type == 0) ? cartMapper.getCharts(userId, false, null) : cartMapper.getCharts(userId, true, null);

        int goodsCount = 0;
        double goodsAmount = 0.0;
        int checkedGoodsCount = 0;
        double checkedGoodsAmount = 0.0;
        int numberChange = 0;

        Map<String, ProductPO> productInfoListGroup = productMapper.findByIdList(cartList.stream().map(CartPO::getProductId).collect(Collectors.toList())).stream().collect(Collectors.toMap(ProductPO::getId, e -> e));
        Map<String, GoodsPO> goodsInfoListGroup = goodsMapper.findByIdList(cartList.stream().map(CartPO::getGoodsId).collect(Collectors.toList())).stream().collect(Collectors.toMap(GoodsPO::getId, e -> e));
        List<String> productIdTODeleteInChart = new ArrayList<>();
        List<CartPO> cartItemTOUpdate = new ArrayList<>();
        for (CartPO cartItem : cartList) {
            ProductPO product = productInfoListGroup.get(cartItem.getProductId());
            if (product == null) {
                productIdTODeleteInChart.add(cartItem.getProductId());
            } else {
                Long retailPrice = product.getRetailPrice();
                Integer productNum = product.getGoodsNumber();
                if (productNum <= 0 || !product.getIsOnSale()) {
                    cartItem.setChecked(false);
                    cartItem.setNumber(0);
                } else if (productNum < cartItem.getNumber()) {
                    cartItem.setNumber(productNum);
                    numberChange = 1;
                } else if (cartItem.getNumber() == 0) {
                    cartItem.setNumber(1);
                    numberChange = 1;
                }
                goodsCount += cartItem.getNumber();
                goodsAmount += cartItem.getNumber() * retailPrice;
                cartItem.setRetailPrice(retailPrice);
                if (cartItem.getChecked() && productNum > 0) {
                    checkedGoodsCount += cartItem.getNumber();
                    checkedGoodsAmount += cartItem.getNumber() * retailPrice;
                }
                GoodsPO goods = goodsInfoListGroup.get(cartItem.getGoodsId());
                cartItem.setListPicUrl(goods.getListPicUrl());
//                cartItem.setWeightCount(cartItem.getNumber() * cartItem.getGoodsWeight());
                cartItemTOUpdate.add(cartItem);
            }
            cartMapper.updateCart(cartItemTOUpdate);
        }
        if (CollUtil.isNotEmpty(productIdTODeleteInChart)) {
            cartMapper.deleteProducts(userId, productIdTODeleteInChart);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("cartList", cartList);
        Map<String, Object> cartTotal = new HashMap<>();
        cartTotal.put("goodsCount", goodsCount);
        cartTotal.put("goodsAmount", String.format("%.2f", goodsAmount));
        cartTotal.put("checkedGoodsCount", checkedGoodsCount);
        cartTotal.put("checkedGoodsAmount", String.format("%.2f", checkedGoodsAmount));
        cartTotal.put("user_id", userId);
        cartTotal.put("numberChange", numberChange);
        result.put("cartTotal", cartTotal);

        return result;
    }

    public void addAgain(String goodsId, String productId, int number) {
        String userId = getLoginUserId();
        long currentTime = System.currentTimeMillis();

        GoodsPO goods = goodsMapper.findById(goodsId);
        if (goods == null || !goods.getIsOnSale()) {
            throw new RuntimeException("商品已下架");
        }

        ProductPO product = productMapper.findById(productId);
        if (product == null || product.getGoodsNumber() < number) {
            throw new RuntimeException("库存不足");
        }

        CartPO cart = CollUtil.getFirst(cartMapper.getCharts(userId, null, productId));
        Long retailPrice = product.getRetailPrice();

        if (cart == null) {
            List<String> goodsSpecificationValues = productMapper.findSpecificationValues(productId, null);
            String specificationNameValue = String.join(";", goodsSpecificationValues);

            CartPO newCart = new CartPO();
            newCart.setGoodsId(goodsId);
            newCart.setProductId(productId);
            newCart.setGoodsSn(product.getGoodsSn());
            newCart.setGoodsName(goods.getName());
            newCart.setGoodsWeight(product.getGoodsWeight());
            newCart.setListPicUrl(goods.getListPicUrl());
            newCart.setNumber(number);
            newCart.setUserId(userId);
            newCart.setRetailPrice(retailPrice);
            newCart.setAddPrice(retailPrice);
            newCart.setGoodsSpecifitionNameValue(specificationNameValue);
            newCart.setChecked(true);
            newCart.setAddTime(currentTime);
            cartMapper.insert(newCart);
        } else {
            if (product.getGoodsNumber() < (number + cart.getNumber())) {
                throw new RuntimeException("库存都不够啦");
            }
            cartMapper.updateAddAgain(retailPrice, true, number, cart.getId());
        }
    }

    private String getLoginUserId() {
        // Implement your user authentication logic here
        return "";
    }


    @Autowired
    private AddressMapper addressMapper;


    @Transactional
    public void deleteProducts(String userId, List<String> productIds) {
        cartMapper.deleteProducts(userId, productIds);
    }

    public List<CartPO> getCarts(String userId, Boolean isFast, String productId) {
        return cartMapper.getCharts(userId, isFast, productId);
    }

    public void updateFastCart(String userId) {
        cartMapper.deleteFastCart(userId);
    }

    public Map<String, Object> checkout(String userId, Long orderFrom, Integer type, String addressId, Integer addType) {
        Map<String, Object> result = new HashMap<>();
        List<CartPO> cartList = getCarts(userId, false, null);
        double freightPrice = calculateFreight(cartList, addressId);

        double goodsTotalPrice = cartList.stream()
                .filter(CartPO::getChecked)
                .mapToDouble(cart -> cart.getNumber() * cart.getRetailPrice())
                .sum();

        double orderTotalPrice = goodsTotalPrice + freightPrice;

        result.put("freightPrice", freightPrice);
        result.put("goodsTotalPrice", goodsTotalPrice);
        result.put("orderTotalPrice", orderTotalPrice);
        result.put("actualPrice", orderTotalPrice);
        result.put("checkedGoodsList", cartList);

        AddressPO checkedAddress = getAddress(userId, addressId);
        result.put("checkedAddress", checkedAddress);

        return result;
    }

    private double calculateFreight(List<CartPO> cartList, String addressId) {
        // Implement freight calculation logic
        return 0.0;
    }

    private AddressPO getAddress(String userId, String addressId) {
        if (StrUtil.isNotEmpty(addressId)) {
            return addressMapper.getDefaultAddress(userId);
        } else {
            return addressMapper.findById(addressId);
        }
    }
}