package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.mapper.AdMapper;
import com.example.po.AdPO;
import com.example.utils.MySnowFlakeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdService {

    @Autowired
    private AdMapper adMapper;

    public Map<String, Object> getAdList(int page, int size) {
        List<AdPO> ads = adMapper.findExpiredAdsPage(page, size);
        Map<String, Object> result = new HashMap<>();
        result.put("data", ads);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    public void updateSortOrder(String id, int sort) {
        adMapper.updateSortOrder(id, sort);
    }

    public AdPO getAdInfo(String id) {
        return adMapper.findById(id);
    }

    public void saveAd(AdPO ad) {
        if (StrUtil.isNotEmpty(ad.getId())) {
            adMapper.updateEndTime(ad.getId(), System.currentTimeMillis());
        } else {
            AdPO adPO = new AdPO(MySnowFlakeGenerator.next(), ad.getLinkType(), ad.getLink(), ad.getGoodsId(), ad.getImageUrl(), ad.getEndTime(), ad.getEnabled(), ad.getSortOrder(), false);
            if (ad.getLinkType() == 0) {
                adPO.setLink("");
            } else {
                adPO.setGoodsId("");
            }
            adMapper.insert(adPO);
        }
    }


    public void destroyAd(String id) {
        adMapper.delete(id);
    }

    public void updateSaleStatus(String id, boolean enabled) {
        adMapper.updateEnable(id, enabled);
    }
}
