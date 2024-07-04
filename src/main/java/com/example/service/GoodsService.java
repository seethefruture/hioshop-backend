package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.mapper.*;
import com.example.po.Category;
import com.example.po.FreightTemplate;
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

    public Long store(Map<String, Object> payload) {
        Map<String, Object> values = (Map<String, Object>) payload.get("info");
        List<Map<String, Object>> specData = (List<Map<String, Object>>) payload.get("specData");
        Integer specValue = (Integer) payload.get("specValue");
        String cateId = (String) payload.get("cateId");

        String goodsId = String.valueOf(values.get("id"));
        values.put("category_id", cateId);
        values.put("is_index", values.get("is_index") != null && (Boolean) values.get("is_index") ? 1 : 0);
        values.put("is_new", values.get("is_new") != null && (Boolean) values.get("is_new") ? 1 : 0);

        String id = String.valueOf(values.get("id"));

        if (StrUtil.isNotEmpty(id)) {
            goodsMapper.updateStore(cateId, (Boolean) values.get("is_index"), (Boolean) values.get("is_new"), id);
            cartMapper.updateCartGoodsById(id, values);
            productMapper.markProductDeletedByGoodsId(id);
            goodsSpecificationMapper.markGoodsSpecificationDeletedByGoodsId(id);

            for (Map<String, Object> item : specData) {
                Long itemId = ((Number) item.get("id")).longValue();
                if (itemId > 0) {
                    cartMapper.updateCartProduct(itemId, item);
                    item.put("is_delete", 0);
                    productMapper.updateProductById(itemId, item);

                    Map<String, Object> specificationData = Map.of(
                            "value", item.get("value"),
                            "specification_id", specValue,
                            "is_delete", 0
                    );
                    goodsSpecificationMapper.updateGoodsSpecification(item.get("goods_specification_ids"), specificationData);
                } else {
                    Map<String, Object> specificationData = Map.of(
                            "value", item.get("value"),
                            "goods_id", id,
                            "specification_id", specValue
                    );
                    Long specId = goodsSpecificationMapper.add(specificationData);
                    item.put("goods_specification_ids", specId);
                    item.put("goods_id", id);
                    productMapper.add(item);
                }
            }

            List<Map<String, Object>> gallery = (List<Map<String, Object>>) values.get("gallery");
            for (int i = 0; i < gallery.size(); i++) {
                Map<String, Object> item = gallery.get(i);
                Long itemId = ((Number) item.get("id")).longValue();
                if ((Integer) item.get("is_delete") == 1 && itemId > 0) {
                    goodsGalleryMapper.markGoodsGalleryDeleted(itemId);
                } else if ((Integer) item.get("is_delete") == 0 && itemId > 0) {
                    goodsGalleryMapper.updateGoodsGallerySortOrder(itemId, i);
                } else if ((Integer) item.get("is_delete") == 0 && itemId == 0) {
                    goodsGalleryMapper.addGoodsGallery(id, item.get("url").toString(), i);
                }
            }
        } else {
            goodsId = goodsMapper.add(values);
            for (Map<String, Object> item : specData) {
                Map<String, Object> specificationData = Map.of(
                        "value", item.get("value"),
                        "goods_id", goodsId,
                        "specification_id", specValue
                );
                Long specId = goodsSpecificationMapper.add(specificationData);
                item.put("goods_specification_ids", specId);
                item.put("goods_id", goodsId);
                item.put("is_on_sale", 1);
                productMapper.add(item);
            }

            List<Map<String, Object>> gallery = (List<Map<String, Object>>) values.get("gallery");
            for (int i = 0; i < gallery.size(); i++) {
                Map<String, Object> item = gallery.get(i);
                goodsGalleryMapper.addGoodsGallery(goodsId, item.get("url").toString(), i);
            }
        }

        List<Map<String, Object>> products = productMapper.findOnSaleProductsByGoodsId(goodsId);
        if (products.size() > 1) {
            Long goodsNum = productMapper.sumGoodsNumberByGoodsId(goodsId);
            List<Long> retailPrices = productMapper.findRetailPricesByGoodsId(goodsId);
            List<Long> costs = productMapper.findCostsByGoodsId(goodsId);

            Long maxPrice = retailPrices.stream().max(Long::compare).orElse(0L);
            Long minPrice = retailPrices.stream().min(Long::compare).orElse(0L);
            Long maxCost = costs.stream().max(Long::compare).orElse(0L);
            Long minCost = costs.stream().min(Long::compare).orElse(0L);

            String goodsPrice = minPrice.equals(maxPrice) ? minPrice.toString() : minPrice + "~" + maxPrice;
            String costPrice = minCost + "~" + maxCost;

            goodsMapper.updateGoodsPrices(goodsId, goodsNum, goodsPrice, costPrice, minPrice, minCost);
        } else {
            Map<String, Object> product = products.get(0);
            Map<String, Object> info = Map.of(
                    "goods_number", product.get("goods_number"),
                    "retail_price", product.get("retail_price"),
                    "cost_price", product.get("cost"),
                    "min_retail_price", product.get("retail_price"),
                    "min_cost_price", product.get("cost")
            );
            goodsMapper.updateGoodsInfo(goodsId, info);
        }

        return goodsId;
    }

    public Map<String, Object> getGoodsDetail(String goodsId, String userId) {
        Goods info = goodsMapper.findById(goodsId);
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
        return goodsMapper.findById(goodsId);
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
        return goodsMapper.countOnSale();
    }

    public JSONObject getExpressData() {
        List<FreightTemplate> freightTemplates = freightTemplateMapper.findFreightTemplates();
        Map<String, String> kdData = freightTemplates.stream().collect(Collectors.toMap(FreightTemplate::getId, FreightTemplate::getName));

        List<Category> categories = categoryMapper.selectTopCategory();
        Map<String, String> cateData = categories.stream().collect(Collectors.toMap(Category::getId, Category::getName));
        return new JSONObject().fluentPut("kd", kdData).fluentPut("cate", cateData);
    }

    public String copyGoods(String goodsId) {
        Goods data = goodsMapper.findById(goodsId);
        String insertId = MySnowFlakeGenerator.next();
        data.setId(insertId);
        data.setIsOnSale(false);
        goodsMapper.insert(data);

        List<GoodsGallery> goodsGallery = goodsGalleryMapper.findByGoodsId(goodsId);
        for (GoodsGallery item : goodsGallery) {
            GoodsGallery gallery = new GoodsGallery();
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
        List<Goods> allGoods = goodsMapper.selectAll();
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
        goodsMapper.updateSaleStatus(id, status);
        productMapper.updateSaleStatusByGoodsId(id, status);
    }
}
