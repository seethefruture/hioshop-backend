package com.example.service;

import com.example.mapper.*;
import com.example.vo.Ad;
import com.example.vo.Category;
import com.example.vo.Goods;
import com.example.vo.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AppService {

    @Autowired
    private AdMapper adMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CartMapper cartMapper;

    public Map<String, Object> getAppInfo() {
        String userId = getLoginUserId();

        List<Ad> banner = adMapper.findExpiredAds();
        List<Notice> notice = noticeMapper.findActiveNotices();
        List<Category> channel = categoryMapper.findChannelCategories();
        List<Category> categoryList = categoryMapper.findShowCategories(); // img_url as banner, p_height as height

        List<Goods> allCategoryGoods = goodsMapper.findCategoryGoods(categoryList.stream().map(Category::getId).collect(Collectors.toList()));
        Map<String, List<Goods>> allCategoryGoodsGroupByCategory = allCategoryGoods.stream().collect(Collectors.groupingBy(Goods::getCategoryId));
        for (Category categoryItem : categoryList) {
            categoryItem.setGoodsList(allCategoryGoodsGroupByCategory.get(categoryItem.getId()));
        }
        Integer cartCount = cartMapper.getCartCountByUserId(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("channel", channel);
        data.put("banner", banner);
        data.put("notice", notice);
        data.put("categoryList", categoryList);
        data.put("cartCount", cartCount);
        return data;
    }

    private String getLoginUserId() {
        // Implement your logic to get the logged-in user ID
        return "";
    }
}
