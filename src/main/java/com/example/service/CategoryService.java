package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.mapper.CategoryMapper;
import com.example.mapper.GoodsMapper;
import com.example.vo.Category;
import com.example.vo.Goods;
import com.example.utils.MySnowFlakeGenerator;
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
        List<Category> categories = categoryMapper.selectRootCategories();
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

    public int updateSortOrder(Long id, Integer sort) {
        return categoryMapper.updateSortOrder(id, sort);
    }

    public List<Category> getTopCategory() {
        return categoryMapper.selectTopCategory();
    }

    public Category getCategoryById(Long id) {
        return categoryMapper.selectCategoryById(id);
    }

    public void saveOrUpdateCategory(Category category) {
        if (StrUtil.isNotEmpty(category.getId())) {
            categoryMapper.updateCategory(category);
        } else {
            category.setId(MySnowFlakeGenerator.next());
            categoryMapper.insertCategory(category);
        }
    }

    public boolean deleteCategory(Long id) {
        List<Category> subCategories = categoryMapper.selectSubCategories(id);
        if (!subCategories.isEmpty()) {
            return false;
        } else {
            categoryMapper.deleteCategory(id);
            return true;
        }
    }

    public void updateShowStatus(Long id, boolean status) {
        categoryMapper.updateShowStatus(id, status ? 1 : 0);
    }

    public void updateChannelStatus(Long id, boolean status) {
        categoryMapper.updateChannelStatus(id, status ? 1 : 0);
    }

    public void updateCategoryStatus(Long id, boolean status) {
        categoryMapper.updateCategoryStatus(id, status ? 1 : 0);
    }

    public void deleteBannerImage(Long id) {
        categoryMapper.deleteBannerImage(id);
    }

    public void deleteIconImage(Long id) {
        categoryMapper.deleteIconImage(id);
    }
}
