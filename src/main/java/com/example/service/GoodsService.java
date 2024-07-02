package com.example.service;

import com.alibaba.fastjson.JSONObject;
import com.example.mapper.FootprintMapper;
import com.example.mapper.GoodsGalleryMapper;
import com.example.mapper.GoodsMapper;
import com.example.mapper.SearchHistoryMapper;
import com.example.po.Footprint;
import com.example.po.Goods;
import com.example.po.GoodsGallery;
import com.example.po.SearchHistory;
import com.example.mapper.GoodsSpecificationMapper;
import com.example.mapper.SpecificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.mapper.ProductMapper;
import com.example.po.Product;
import com.example.po.GoodsSpecification;
import com.example.po.Specification;

import java.util.*;

@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private FootprintMapper footprintMapper;

    @Autowired
    private GoodsGalleryMapper goodsGalleryMapper;

    @Autowired
    private SearchHistoryMapper searchHistoryMapper;
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private GoodsSpecificationMapper goodsSpecificationMapper;

    @Autowired
    private SpecificationMapper specificationMapper;

    public List<Goods> getAllGoods() {
        return goodsMapper.selectAll();
    }

    /**
     * 获取商品的product
     *
     * @param goodsId
     * @return List<Product>
     */
    public List<Product> getProductList(String goodsId) {
        return productMapper.selectProductList(goodsId);
    }

    /**
     * 获取商品的规格信息
     *
     * @param goodsId
     * @return Map<String, Object>
     */
    public Map<String, Object> getSpecificationList(String goodsId) {
        List<GoodsSpecification> info = goodsSpecificationMapper.selectSpecificationList(goodsId);
        for (GoodsSpecification item : info) {
            Product product = productMapper.findProductByGoodsDetail(goodsId, item.getId(), 0);
            item.setGoodsNumber(product.getGoodsNumber());
        }
        String specId = info.get(0).getSpecificationId();
        Specification specification = specificationMapper.findById(specId);
        String name = specification.getName();
        return new JSONObject().fluentPut("specification_id", specId).fluentPut("name", name).fluentPut("valueList", info);
    }

    public Map<String, Object> getGoodsDetail(String goodsId, String userId) {
        Goods info = goodsMapper.selectById(goodsId);
        if (info == null || info.getIsDelete() == 1) {
            return null;
        }
        List<GoodsGallery> gallery = goodsGalleryMapper.selectByGoodsId(goodsId);
        List<Product> productList = productMapper.selectProductList(goodsId);
        int goodsNumber = productList.stream().mapToInt(Product::getGoodsNumber).sum();
        List<GoodsSpecification> specificationList = goodsSpecificationMapper.selectSpecificationList(goodsId);
        info.setGoodsNumber(goodsNumber);
        Map<String, Object> result = new HashMap<>();
        result.put("info", info);
        result.put("gallery", gallery);
        result.put("specificationList", specificationList);
        result.put("productList", productList);
        footprintMapper.insert(new Footprint(userId, goodsId));
        return result;
    }

    public Goods getGoodsShare(String goodsId) {
        return goodsMapper.selectShareById(goodsId);
    }

    public List<Goods> getGoodsList(String userId, String keyword, String sort, String order, String sales) {
        Map<String, Object> params = new HashMap<>();
        params.put("isOnSale", 1);
        params.put("isDelete", 0);
        if (keyword != null && !keyword.isEmpty()) {
            params.put("keyword", "%" + keyword + "%");
            searchHistoryMapper.insert(new SearchHistory(userId, keyword, System.currentTimeMillis() / 1000));
        }

        if ("price".equals(sort)) {
            params.put("orderBy", "retail_price");
            params.put("order", order);
        } else if ("sales".equals(sort)) {
            params.put("orderBy", "sell_volume");
            params.put("order", sales);
        } else {
            params.put("orderBy", "sort_order");
            params.put("order", "asc");
        }

        return goodsMapper.selectByParams(params);
    }

    public int getGoodsCount() {
        return goodsMapper.count();
    }
}
