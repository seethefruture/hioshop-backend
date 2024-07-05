package com.example.service;

import com.example.mapper.*;
import com.example.po.AdPO;
import com.example.po.CategoryPO;
import com.example.po.GoodsPO;
import com.example.po.NoticePO;
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

        List<AdPO> banner = adMapper.findExpiredAds();
        List<NoticePO> notice = noticeMapper.findActiveNotices();
        List<CategoryPO> channel = categoryMapper.findChannelCategories();
        List<CategoryPO> categoryList = categoryMapper.findShowCategories(); // img_url as banner, p_height as height

        List<GoodsPO> allCategoryGoods = goodsMapper.findCategoryGoods(categoryList.stream().map(CategoryPO::getId).collect(Collectors.toList()));
        Map<String, List<GoodsPO>> allCategoryGoodsGroupByCategory = allCategoryGoods.stream().collect(Collectors.groupingBy(GoodsPO::getCategoryId));
        for (CategoryPO categoryItem : categoryList) {
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
