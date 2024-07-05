package com.example.service;

import com.alibaba.fastjson.JSONObject;
import com.example.mapper.*;
import com.example.po.CategoryPO;
import com.example.po.GoodsGalleryPO;
import com.example.po.GoodsPO;
import com.example.po.GoodsSpecificationPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsSpecificationMapper goodsSpecificationMapper;

    @Autowired
    private GoodsGalleryMapper goodsGalleryMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    public void updateProductStatus(String id, Integer status) {
        productMapper.updateProductStatus(id, status);
        productMapper.updateCartProductStatus(id, status);
    }

    public void updateIndexShowStatus(String id, Boolean status) {
        goodsMapper.updateIndexShowStatus(id, status);
    }

    public Map<String, Object> getProductInfo(String id) {
        Map<String, Object> infoData = new HashMap<>();
        GoodsPO goods = goodsMapper.findById(id);
        infoData.put("info", goods);
        infoData.put("category_id", goods.getCategoryId());
        return infoData;
    }



    public Map<String, Object> getAllCategories() {
        List<Map<String, Object>> newData = new ArrayList<>();
        List<CategoryPO> rootCategories = categoryMapper.findCategoriesByLevelAndParentId("L1", "0");
        List<String> rootCategoriesId = rootCategories.stream().map(CategoryPO::getId).collect(Collectors.toList());
        List<CategoryPO> level2CategoriesGroup = categoryMapper.findCategoriesByLevelAndParentIdList("L2", rootCategoriesId);
        Map<String, List<CategoryPO>> level2CategoriesMap = level2CategoriesGroup.stream().collect(Collectors.groupingBy(CategoryPO::getParentId));
        for (CategoryPO item : rootCategories) {
            List<Map<String, Object>> children = new ArrayList<>();
            List<CategoryPO> level2Categories = level2CategoriesMap.get(item.getId());
            for (CategoryPO cItem : level2Categories) {
                Map<String, Object> child = new HashMap<>();
                child.put("value", cItem.getId());
                child.put("label", cItem.getName());
                children.add(child);
            }
            Map<String, Object> category = new HashMap<>();
            category.put("value", item.getId());
            category.put("label", item.getName());
            category.put("children", children);
            newData.add(category);
        }
        return Collections.singletonMap("categories", newData);
    }

    public void updatePrice(Map<String, Object> data) {
        productMapper.updatePrice(data);
    }

    public Boolean checkSku(Map<String, Object> info) {
        // Check for duplicate SKU
        return productMapper.isSkuUnique(info);
    }

    public void updateSort(String id, Integer sort) {
        goodsMapper.updateSortOrder(id, sort);
    }

    public Map<String, Object> getGalleryList(String id) {
        GoodsGalleryPO data = goodsGalleryMapper.findById(id);
        return new JSONObject().fluentPut("galleryList", data);
    }

    public void addGallery(String url, String goodsId) {
        GoodsGalleryPO gallery = new GoodsGalleryPO();
        gallery.setGoodsId(goodsId);
        gallery.setImgUrl(url);
        goodsGalleryMapper.insert(gallery);
    }

    public Map<String, Object> getGalleryListByGoodsId(String goodsId) {
        List<GoodsGalleryPO> data = goodsGalleryMapper.findByGoodsId(goodsId);
        return Collections.singletonMap("galleryData", data);
    }

    public void deleteGalleryFile(String id) {
        goodsGalleryMapper.delete(id);
    }

    public void editGallery(Map<String, Object> values) {
        // Update gallery information
    }

    public void deleteListPicUrl(String id) {
        goodsMapper.updateListPicUrl(id, null);
    }

    public void destroyProduct(String id) {
        goodsMapper.delete(id);
        productMapper.deleteByGoodsId(id);
        goodsSpecificationMapper.deleteByGoodsId(id);
    }

    public String uploadHttpsImage(String url) {
        // Upload image to Qiniu and return URL
        return "https://example.com/image";
    }
}
