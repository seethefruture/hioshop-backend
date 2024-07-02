package com.example.service;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.mapper.*;
import com.example.vo.Footprint;
import com.example.vo.Goods;
import com.example.vo.GoodsGallery;
import com.example.vo.SearchHistory;
import com.example.utils.MySnowFlakeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.vo.Product;
import com.example.vo.GoodsSpecification;
import com.example.vo.Specification;

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
    @Autowired
    private CategoryMapper categoryMapper;

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

    public List<Goods> getGoods(int page, int size, String name) {
        List<Goods> goodsList = goodsMapper.findGoods(name, page, size);
        for (Goods item : goodsList) {
            item.setCategoryName(categoryMapper.findCategoryNameById(item.getCategoryId()));
            item.setIsOnSale(item.getIsOnSale());
            item.setIsIndex(item.getIsIndex());

            List<Product> products = productMapper.findProductsByGoodsId(item.getId());
            for (Product product : products) {
                GoodsSpecification spec = CollUtil.getFirst(goodsSpecificationMapper.selectSpecificationList(product.getGoodsSpecificationIds()));
                product.setValue(spec.getValue());
            }
            item.setProducts(products);
        }
        return goodsList;
    }

    public List<String> getExpressData() {
        List<String> kd = new ArrayList<>();
        List<String> cate = new ArrayList<>();
        List<String> kdData = goodsMapper.findFreightTemplates();
        for (String item : kdData) {
            kd.add(item);
        }
        List<String> cateData = categoryMapper.findCategories();
        for (String item : cateData) {
            cate.add(item);
        }
        List<String> infoData = new ArrayList<>();
        infoData.addAll(kd);
        infoData.addAll(cate);
        return infoData;
    }

    public String copyGoods(String goodsId) {
        Goods data = goodsMapper.findGoodsById(goodsId);
        String insertId = MySnowFlakeGenerator.next();
        data.setId(insertId);
        data.setIsOnSale(false);
        goodsMapper.insertGoods(data);

        List<GoodsGallery> goodsGallery = goodsGalleryMapper.findGoodsGalleryByGoodsId(goodsId);
        for (GoodsGallery item : goodsGallery) {
            GoodsGallery gallery = new GoodsGallery();
            gallery.setImgUrl(item.getImgUrl());
//            gallery.setSortOrder(item.getSortOrder());
            gallery.setGoodsId(insertId);
            goodsGalleryMapper.insertGoodsGallery(gallery);
        }
        return insertId;
    }

    public void updateStock(String goodsSn, int goodsNumber) {
        productMapper.updateGoodsNumberBySn(goodsSn, goodsNumber);
    }

    public void updateGoodsNumber() {
        List<Goods> allGoods = goodsMapper.findAllGoods();
        for (Goods item : allGoods) {
            int goodsSum = productMapper.sumGoodsNumberByGoodsId(item.getId());
            goodsMapper.updateGoodsNumber(item.getId(), goodsSum);
            // Simulate delay
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Goods> getOnSaleGoods(int page, int size) {
        List<Goods> goodsList = goodsMapper.findOnSaleGoods(page, size);
        for (Goods item : goodsList) {
            item.setCategoryName(categoryMapper.findCategoryNameById(item.getCategoryId()));
            List<Product> products = productMapper.findProductsByGoodsId(item.getId());
            for (Product product : products) {
                GoodsSpecification spec = CollUtil.getFirst(goodsSpecificationMapper.selectSpecificationList(product.getGoodsSpecificationIds()));
                product.setValue(spec.getValue());
            }
            item.setProducts(products);
        }
        return goodsList;
    }

    public List<Goods> getOutOfStockGoods(int page, int size) {
        List<Goods> goodsList = goodsMapper.findOutOfStockGoods(page, size);
        for (Goods item : goodsList) {
            item.setCategoryName(categoryMapper.findCategoryNameById(item.getCategoryId()));
            List<Product> products = productMapper.findProductsByGoodsId(item.getId());
            for (Product product : products) {
                GoodsSpecification spec = CollUtil.getFirst(goodsSpecificationMapper.selectSpecificationList(product.getGoodsSpecificationIds()));
                product.setValue(spec.getValue());
            }
            item.setProducts(products);
        }
        return goodsList;
    }

    public List<Goods> getDroppedGoods(int page, int size) {
        List<Goods> goodsList = goodsMapper.findDroppedGoods(page, size);
        for (Goods item : goodsList) {
            item.setCategoryName(categoryMapper.findCategoryNameById(item.getCategoryId()));
            List<Product> products = productMapper.findProductsByGoodsId(item.getId());
            for (Product product : products) {
                GoodsSpecification spec = CollUtil.getFirst(goodsSpecificationMapper.selectSpecificationList(product.getGoodsSpecificationIds()));
                product.setValue(spec.getValue());
            }
            item.setProducts(products);
        }
        return goodsList;
    }

    public List<Goods> sortGoods(int page, int size, int index) {
        List<Goods> goodsList;
        switch (index) {
            case 1:
                goodsList = goodsMapper.findGoodsSortedBySellVolume(page, size);
                break;
            case 2:
                goodsList = goodsMapper.findGoodsSortedByRetailPrice(page, size);
                break;
            case 3:
                goodsList = goodsMapper.findGoodsSortedByNumber(page, size);
                break;
            default:
                goodsList = goodsMapper.findGoods(null, page, size);
                break;
        }
        for (Goods item : goodsList) {
            item.setCategoryName(categoryMapper.findCategoryNameById(item.getCategoryId()));
            List<Product> products = productMapper.findProductsByGoodsId(item.getId());
            for (Product product : products) {
                GoodsSpecification spec = CollUtil.getFirst(goodsSpecificationMapper.selectSpecificationList(product.getGoodsSpecificationIds()));
                product.setValue(spec.getValue());
            }
            item.setProducts(products);
        }
        return goodsList;
    }

    public void updateSaleStatus(String id, boolean status) {
        int saleStatus = status ? 1 : 0;
        goodsMapper.updateSaleStatus(id, saleStatus);
        productMapper.updateSaleStatusByGoodsId(id, saleStatus);
    }
}
