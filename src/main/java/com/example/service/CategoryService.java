package com.example.service;

import com.example.mapper.CategoryMapper;
import com.example.mapper.GoodsMapper;
import com.example.po.Category;
import com.example.po.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    public Map<String, Object> getCategoryList(Long categoryId) {
        List<Category> categories = categoryMapper.selectCategories(10); // TODO 这里好像有问题
        Category currentCategory = null;
        if (categoryId != null) {
            currentCategory = categoryMapper.selectCategoryById(categoryId);
        }
        if (currentCategory == null && !categories.isEmpty()) {
            currentCategory = categories.get(0);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("categoryList", categories);
        result.put("currentCategory", currentCategory);
        return result;
    }

    public List<Long> getChildCategoryId(Long parentId) {
        return categoryMapper.findIdsByParentId(parentId);
    }

    public List<Long> getCategoryWhereIn(Long categoryId) {
        List<Long> childIds = getChildCategoryId(categoryId);
        childIds.add(categoryId);
        return childIds;
    }

    public Category getCategory(Long categoryId) {
        return categoryMapper.selectCategoryById(categoryId);
    }

    public Map<String, Object> getGoodsList(int page, int size, Long categoryId) {
        int offset = (page - 1) * size;
        List<Goods> goodsList;
        int total;
        goodsList = goodsMapper.selectGoods(1, 0, categoryId, "asc", offset, size);
        total = goodsMapper.countGoods(1, 0, categoryId);
        Map<String, Object> result = new HashMap<>();
        result.put("list", goodsList);
        result.put("total", total);
        return result;
    }
}
