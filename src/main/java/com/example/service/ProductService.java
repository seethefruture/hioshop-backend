package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.mapper.*;
import com.example.po.Category;
import com.example.vo.Goods;
import com.example.vo.GoodsSpecification;
import com.example.vo.GoodsGallery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Goods goods = goodsMapper.findById(id);
        infoData.put("info", goods);
        infoData.put("category_id", goods.getCategoryId());
        return infoData;
    }

    public Map<String, Object> getAllSpecifications() {
        List<Map<String, Object>> specOptionsData = new ArrayList<>();
        List<GoodsSpecification> specInfo = goodsSpecificationMapper.findAll();
        for (GoodsSpecification spitem : specInfo) {
            Map<String, Object> info = new HashMap<>();
            info.put("value", spitem.getId());
            info.put("label", spitem.getName());
            specOptionsData.add(info);
        }
        return Collections.singletonMap("specifications", specOptionsData);
    }

    public Map<String, Object> getAllCategory1() {
        List<Map<String, Object>> newData = new ArrayList<>();
        List<Goods> level1Categories = goodsMapper.findCategoriesByLevelAndParentId();
        List<Goods> level2Categories = goodsMapper.findLevel2Categories();

        for (Goods item : level1Categories) {
            List<Map<String, Object>> children = new ArrayList<>();
            for (Goods citem : level2Categories) {
                if (citem.getParentId().equals(item.getId())) {
                    Map<String, Object> child = new HashMap<>();
                    child.put("value", citem.getId());
                    child.put("label", citem.getName());
                    children.add(child);
                }
            }
            Map<String, Object> category = new HashMap<>();
            category.put("value", item.getId());
            category.put("label", item.getName());
            category.put("children", children);
            newData.add(category);
        }
        return Collections.singletonMap("categories", newData);
    }

    public Map<String, Object> getAllCategories() {
        List<Map<String, Object>> newData = new ArrayList<>();
        List<Category> rootCategories = categoryMapper.findCategoriesByLevelAndParentId("L1", "0");
        List<String> rootCategoriesId = rootCategories.stream().map(Category::getId).collect(Collectors.toList());
        List<Category> level2CategoriesGroup = categoryMapper.findCategoriesByLevelAndParentIdList("L2", rootCategoriesId);
        Map<String, List<Category>> level2CategoriesMap = level2CategoriesGroup.stream().collect(Collectors.groupingBy(Category::getParentId));
        for (Category item : rootCategories) {
            List<Map<String, Object>> children = new ArrayList<>();
            List<Category> level2Categories = level2CategoriesMap.get(item.getId());
            for (Category cItem : level2Categories) {
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
        List<GoodsGallery> data = goodsGalleryMapper.findById(id);
        return Collections.singletonMap("galleryList", data);
    }

    public void addGallery(String url, String goodsId) {
        GoodsGallery gallery = new GoodsGallery();
        gallery.setGoodsId(goodsId);
        gallery.setImgUrl(url);
        goodsGalleryMapper.insert(gallery);
    }

    public Map<String, Object> getGalleryListByGoodsId(String goodsId) {
        List<GoodsGallery> data = goodsGalleryMapper.findByGoodsId(goodsId);
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
        productMapper.markProductsAsDeleted(id);
        goodsSpecificationMapper.markSpecificationsAsDeleted(id);
    }

    public String uploadHttpsImage(String url) {
        // Upload image to Qiniu and return URL
        return "https://example.com/image";
    }
}
