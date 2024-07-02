package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.example.mapper.AddressMapper;
import com.example.mapper.CartMapper;
import com.example.mapper.GoodsMapper;
import com.example.mapper.ProductMapper;
import com.example.vo.Address;
import com.example.vo.Cart;
import com.example.vo.Goods;
import com.example.vo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Cart> getGoodsList(String userId) {
        return cartMapper.getCharts(userId, null, null);
    }

    public List<Cart> getCheckedGoodsList(String userId) {
        return cartMapper.getCharts(userId, null, null);
    }

    public int clearBuyGoods(String userId) {
        return cartMapper.deleteCheckedProducts(userId);
    }

    public Map<String, Object> getCarts(int type) {
        String userId = getLoginUserId();
        List<Cart> cartList = (type == 0) ? cartMapper.getCharts(userId, false, null) : cartMapper.getCharts(userId, true, null);

        int goodsCount = 0;
        double goodsAmount = 0.0;
        int checkedGoodsCount = 0;
        double checkedGoodsAmount = 0.0;
        int numberChange = 0;

        Map<String, Product> productInfoListGroup = productMapper.findByIdList(cartList.stream().map(Cart::getProductId).collect(Collectors.toList())).stream().collect(Collectors.toMap(Product::getId, e -> e));
        Map<String, Goods> goodsInfoListGroup = goodsMapper.findByIdList(cartList.stream().map(Cart::getGoodsId).collect(Collectors.toList())).stream().collect(Collectors.toMap(Goods::getId, e -> e));
        List<String> productIdTODeleteInChart = new ArrayList<>();
        List<Cart> cartItemTOUpdate = new ArrayList<>();
        for (Cart cartItem : cartList) {
            Product product = productInfoListGroup.get(cartItem.getProductId());
            if (product == null) {
                productIdTODeleteInChart.add(cartItem.getProductId());
            } else {
                double retailPrice = product.getRetailPrice();
                int productNum = product.getGoodsNumber();
                if (productNum <= 0 || !product.isOnSale()) {
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
                if (cartItem.isChecked() && productNum > 0) {
                    checkedGoodsCount += cartItem.getNumber();
                    checkedGoodsAmount += cartItem.getNumber() * retailPrice;
                }
                Goods goods = goodsInfoListGroup.get(cartItem.getGoodsId());
                cartItem.setListPicUrl(goods.getListPicUrl());
                cartItem.setWeightCount(cartItem.getNumber() * cartItem.getGoodsWeight());
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
        long currentTime = System.currentTimeMillis() / 1000;

        Goods goods = goodsMapper.findById(goodsId);
        if (goods == null || !goods.getIsOnSale()) {
            throw new RuntimeException("商品已下架");
        }

        Product product = productMapper.findById(productId);
        if (product == null || product.getGoodsNumber() < number) {
            throw new RuntimeException("库存不足");
        }

        Cart cart = CollUtil.getFirst(cartMapper.getCharts(userId, null, productId));
        double retailPrice = product.getRetailPrice();

        if (cart == null) {
            List<String> goodsSpecificationValues = productMapper.findSpecificationValues(productId, null);
            String specificationNameValue = String.join(";", goodsSpecificationValues);

            Cart newCart = new Cart();
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
            newCart.setGoodsSpecificationNameValue(specificationNameValue);
            newCart.setChecked(true);
            newCart.setAddTime(currentTime);
            cartMapper.insert(newCart);
        } else {
            if (product.getGoodsNumber() < (number + cart.getNumber())) {
                throw new RuntimeException("库存都不够啦");
            }
            cart.setRetailPrice(retailPrice);
            cart.setChecked(true);
            cart.setNumber(number);
            cartMapper.updateAddAgain(cart);
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

    public List<Cart> getCarts(String userId, Boolean isFast, String productId) {
        return cartMapper.getCharts(userId, isFast, productId);
    }

    public void updateFastCart(String userId) {
        cartMapper.deleteFastCart(userId);
    }

    public Map<String, Object> checkout(String userId, Long orderFrom, Integer type, String addressId, Integer addType) {
        Map<String, Object> result = new HashMap<>();
        List<Cart> cartList = getCarts(userId, false, null);
        double freightPrice = calculateFreight(cartList, addressId);

        double goodsTotalPrice = cartList.stream()
                .filter(Cart::isChecked)
                .mapToDouble(cart -> cart.getNumber() * cart.getRetailPrice())
                .sum();

        double orderTotalPrice = goodsTotalPrice + freightPrice;

        result.put("freightPrice", freightPrice);
        result.put("goodsTotalPrice", goodsTotalPrice);
        result.put("orderTotalPrice", orderTotalPrice);
        result.put("actualPrice", orderTotalPrice);
        result.put("checkedGoodsList", cartList);

        Address checkedAddress = getAddress(userId, addressId);
        result.put("checkedAddress", checkedAddress);

        return result;
    }

    private double calculateFreight(List<Cart> cartList, String addressId) {
        // Implement freight calculation logic
        return 0.0;
    }

    private Address getAddress(String userId, String addressId) {
        if (StrUtil.isNotEmpty(addressId)) {
            return addressMapper.getDefaultAddress(userId);
        } else {
            return addressMapper.findById(addressId);
        }
    }
}