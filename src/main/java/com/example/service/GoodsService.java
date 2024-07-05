package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.mapper.*;
import com.example.po.*;
import com.example.po.GoodsPO;
import com.example.utils.MySnowFlakeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.po.ProductPO;
import com.example.po.SpecificationPO;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private FreightTemplateMapper freightTemplateMapper;
    @Autowired
    private CartMapper cartMapper;

    public List<GoodsPO> getAllGoods() {
        return goodsMapper.selectAll();
    }

    /**
     * 获取商品的product
     *
     * @param goodsId
     * @return List<ProductPO>
     */
    public List<ProductPO> getProductList(String goodsId) {
        return productMapper.selectProductList(goodsId);
    }

    /**
     * 获取商品的规格信息
     *
     * @param goodsId
     * @return Map<String, Object>
     */
    public Map<String, Object> getSpecificationList(String goodsId) {
        List<GoodsSpecificationPO> info = goodsSpecificationMapper.selectSpecificationList(goodsId);
        for (GoodsSpecificationPO item : info) {
            ProductPO product = productMapper.findProductByGoodsDetail(goodsId, item.getId(), 0);
            item.setGoodsNumber(product.getGoodsNumber());
        }
        String specId = info.get(0).getSpecificationId();
        SpecificationPO specification = specificationMapper.findById(specId);
        String name = specification.getName();
        return new JSONObject().fluentPut("specification_id", specId).fluentPut("name", name).fluentPut("valueList", info);
    }

    public String store(Map<String, Object> payload) {
        GoodsPO goods = (GoodsPO) payload.get("info");
        List<ProductPO> specData = (List<ProductPO>) payload.get("specData");
        String specValue = String.valueOf(payload.get("specValue"));
        String cateId = (String) payload.get("cateId");

        String goodsId = goods.getId();
        goods.setCategoryId(cateId);
        if (StrUtil.isNotEmpty(goodsId)) {
            goodsMapper.updateStore(cateId, goods.getIsIndex(), goods.getIsNew(), goodsId);
            for (ProductPO product : specData) {
                String productId = product.getId();
                if (StrUtil.isNotEmpty(productId)) {
                    product.setIsDelete(false);
                    productMapper.deleteByGoodsId(goodsId);
                    goodsSpecificationMapper.updateGoodsSpecification(product.getValue(), specValue, product.getGoodsSpecificationIds());
                } else {
                    String goodsSpecificationId = MySnowFlakeGenerator.next();
                    GoodsSpecificationPO goodsSpecificationPO = new GoodsSpecificationPO(goodsSpecificationId, goodsId, specValue, product.getValue(), "", false);
                    goodsSpecificationMapper.insert(goodsSpecificationPO);
                    product.setGoodsSpecificationIds(goodsSpecificationId);
                    product.setGoodsId(goodsId);
                    productMapper.insert(product);
                }
            }

            List<GoodsGalleryPO> gallery = goods.getGallery();
            for (int i = 0; i < gallery.size(); i++) {
                GoodsGalleryPO goodsGallery = gallery.get(i);
                String goodsGalleryId = goodsGallery.getId();
                if (StrUtil.isNotEmpty(goodsGalleryId)) {
                    goodsGalleryMapper.update(goodsGallery);
                } else {
                    goodsGallery.setId(MySnowFlakeGenerator.next());
                    goodsGalleryMapper.insert(goodsGallery);
                }

            }
        } else {
            goodsMapper.insert(goods);
            for (ProductPO product : specData) {
                String goodsSpecificationId = MySnowFlakeGenerator.next();
                GoodsSpecificationPO goodsSpecificationPO = new GoodsSpecificationPO(goodsSpecificationId, goodsId, specValue, product.getValue(), "", false);
                goodsSpecificationMapper.insert(goodsSpecificationPO);
                product.setGoodsSpecificationIds(goodsSpecificationId);
                product.setGoodsId(goodsId);
                product.setIsOnSale(false);
                productMapper.insert(product);
            }

            List<GoodsGalleryPO> gallery = goods.getGallery();
            for (GoodsGalleryPO item : gallery) {
                goodsGalleryMapper.insert(item);
            }
        }

        List<ProductPO> products = productMapper.findOnSaleProductsByGoodsId(goodsId);
        if (CollUtil.isNotEmpty(products)) {
            Integer goodsNum = productMapper.sumGoodsNumberByGoodsId(goodsId);
            List<Long> retailPrices = productMapper.findProductsByGoodsId(goodsId).stream().map(ProductPO::getRetailPrice).collect(Collectors.toList());
            List<Long> costs = productMapper.findProductsByGoodsId(goodsId).stream().map(ProductPO::getCost).collect(Collectors.toList());

//            BigDecimal maxPrice = retailPrices.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            Long minPrice = retailPrices.stream().min(Long::compareTo).orElse(0L);
//            BigDecimal maxCost = costs.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            Long minCost = costs.stream().min(Long::compareTo).orElse(0L);

            goodsMapper.updateGoodsPrices(goodsId, goodsNum, minPrice, minCost, minPrice, minCost);
        } else {
            throw new RuntimeException("TODO");
        }
        return goodsId;
    }

    public Map<String, Object> getGoodsDetail(String goodsId, String userId) {
        GoodsPO info = goodsMapper.findById(goodsId);
        if (info == null || info.getIsDelete()) {
            return null;
        }
        List<GoodsGalleryPO> gallery = goodsGalleryMapper.selectByGoodsId(goodsId);
        List<ProductPO> productList = productMapper.selectProductList(goodsId);
        int goodsNumber = productList.stream().mapToInt(ProductPO::getGoodsNumber).sum();
        List<GoodsSpecificationPO> specificationList = goodsSpecificationMapper.selectSpecificationList(goodsId);
        info.setGoodsNumber(goodsNumber);
        Map<String, Object> result = new HashMap<>();
        result.put("info", info);
        result.put("gallery", gallery);
        result.put("specificationList", specificationList);
        result.put("productList", productList);
        footprintMapper.insert(new FootprintPO(MySnowFlakeGenerator.next(), userId, goodsId));
        return result;
    }

    public GoodsPO getGoodsShare(String goodsId) {
        return goodsMapper.findById(goodsId);
    }

    public List<GoodsPO> getGoodsList(String userId, String keyword, String sort, String order, String sales) {
        Map<String, Object> params = new HashMap<>();
        params.put("isOnSale", 1);
        params.put("isDelete", 0);
        if (keyword != null && !keyword.isEmpty()) {
            params.put("keyword", "%" + keyword + "%");
            searchHistoryMapper.insert(new SearchHistoryPO(MySnowFlakeGenerator.next(), keyword, "", System.currentTimeMillis(), userId));
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
        return goodsMapper.countOnSale();
    }

    public JSONObject getExpressData() {
        List<FreightTemplatePO> freightTemplatePOS = freightTemplateMapper.findFreightTemplates();
        Map<String, String> kdData = freightTemplatePOS.stream().collect(Collectors.toMap(FreightTemplatePO::getId, FreightTemplatePO::getName));

        List<CategoryPO> categories = categoryMapper.selectTopCategory();
        Map<String, String> cateData = categories.stream().collect(Collectors.toMap(CategoryPO::getId, CategoryPO::getName));
        return new JSONObject().fluentPut("kd", kdData).fluentPut("cate", cateData);
    }

    public String copyGoods(String goodsId) {
        GoodsPO data = goodsMapper.findById(goodsId);
        String insertId = MySnowFlakeGenerator.next();
        data.setId(insertId);
        data.setIsOnSale(false);
        goodsMapper.insert(data);

        List<GoodsGalleryPO> goodsGallery = goodsGalleryMapper.findByGoodsId(goodsId);
        for (GoodsGalleryPO item : goodsGallery) {
            GoodsGalleryPO gallery = new GoodsGalleryPO();
            gallery.setImgUrl(item.getImgUrl());
//            gallery.setSortOrder(item.getSortOrder());
            gallery.setGoodsId(insertId);
            goodsGalleryMapper.insert(gallery);
        }
        return insertId;
    }

    public void updateStock(String goodsSn, int goodsNumber) {
        productMapper.updateGoodsNumberBySn(goodsSn, goodsNumber);
    }

    public void updateGoodsNumber() {
        List<GoodsPO> allGoods = goodsMapper.selectAll();
        for (GoodsPO item : allGoods) {
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

    public List<GoodsPO> getOnSaleGoods(int page, int size) {
        List<GoodsPO> goodsList = goodsMapper.findOnSaleGoods(page, size);
        for (GoodsPO item : goodsList) {
            item.setCategoryName(categoryMapper.findCategoryNameById(item.getCategoryId()));
            List<ProductPO> products = productMapper.findProductsByGoodsId(item.getId());
            for (ProductPO product : products) {
                GoodsSpecificationPO spec = CollUtil.getFirst(goodsSpecificationMapper.selectSpecificationList(product.getGoodsSpecificationIds()));
                product.setValue(spec.getValue());
            }
            item.setProducts(products);
        }
        return goodsList;
    }

    public List<GoodsPO> getOutOfStockGoods(int page, int size) {
        List<GoodsPO> goodsList = goodsMapper.findOutOfStockGoods(page, size);
        for (GoodsPO item : goodsList) {
            item.setCategoryName(categoryMapper.findCategoryNameById(item.getCategoryId()));
            List<ProductPO> products = productMapper.findProductsByGoodsId(item.getId());
            for (ProductPO product : products) {
                GoodsSpecificationPO spec = CollUtil.getFirst(goodsSpecificationMapper.selectSpecificationList(product.getGoodsSpecificationIds()));
                product.setValue(spec.getValue());
            }
            item.setProducts(products);
        }
        return goodsList;
    }

    public List<GoodsPO> getDroppedGoods(int page, int size) {
        List<GoodsPO> goodsList = goodsMapper.findDroppedGoods(page, size);
        for (GoodsPO item : goodsList) {
            item.setCategoryName(categoryMapper.findCategoryNameById(item.getCategoryId()));
            List<ProductPO> products = productMapper.findProductsByGoodsId(item.getId());
            for (ProductPO product : products) {
                GoodsSpecificationPO spec = CollUtil.getFirst(goodsSpecificationMapper.selectSpecificationList(product.getGoodsSpecificationIds()));
                product.setValue(spec.getValue());
            }
            item.setProducts(products);
        }
        return goodsList;
    }

    public List<GoodsPO> sortGoods(int page, int size, int index) {
        List<GoodsPO> goodsList;
        switch (index) {
            case 1:
                goodsList = goodsMapper.findGoodsSortDESC("sell_volume", page, size);
                break;
            case 2:
                goodsList = goodsMapper.findGoodsSortDESC("retail_price", page, size);
                break;
            case 3:
                goodsList = goodsMapper.findGoodsSortDESC("goods_number", page, size);
                break;
            default:
                goodsList = goodsMapper.findGoodsSortDESC("id", page, size);
                break;
        }
        for (GoodsPO item : goodsList) {
            item.setCategoryName(categoryMapper.findCategoryNameById(item.getCategoryId()));
            List<ProductPO> products = productMapper.findProductsByGoodsId(item.getId());
            for (ProductPO product : products) {
                GoodsSpecificationPO spec = CollUtil.getFirst(goodsSpecificationMapper.selectSpecificationList(product.getGoodsSpecificationIds()));
                product.setValue(spec.getValue());
            }
            item.setProducts(products);
        }
        return goodsList;
    }

    public void updateSaleStatus(String id, Boolean status) {
        goodsMapper.updateSaleStatus(id, status);
        productMapper.updateSaleStatusByGoodsId(id, status);
    }
}
