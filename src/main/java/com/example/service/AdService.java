package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.mapper.AdMapper;
import com.example.vo.Ad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdService {

    @Autowired
    private AdMapper adMapper;

    public Map<String, Object> getAdList(int page, int size) {
        List<Ad> ads = adMapper.findExpiredAdsPage(System.currentTimeMillis(), page, size);
        for (Ad ad : ads) {
            if (ad.getEndTime() != 0) {
                ad.setEndTimeFormatted(DateUtil.format(new Date(ad.getEndTime()), "yyyy-MM-dd HH:mm:ss"));
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("data", ads);
        result.put("page", page);
        result.put("size", size);
        return result;
    }

    public void updateSortOrder(int id, int sort) {
        adMapper.updateSortOrder(id, sort);
    }

    public Ad getAdInfo(int id) {
        return adMapper.findById(id);
    }

    public void saveAd(Map<String, Object> values) {
        String id = String.valueOf(values.get("id"));
        values.put("end_time", (int) (System.currentTimeMillis() / 1000));
        if (StrUtil.isNotEmpty(id)) {
            adMapper.updateEndTime(id, System.currentTimeMillis());
        } else {
            if (adMapper.findAdByGoodsId((int) values.get("goods_id")) == null) {
                values.remove("id");
                if ((int) values.get("link_type") == 0) {
                    values.put("link", "");
                } else {
                    values.put("goods_id", 0);
                }
                adMapper.insert(values);
            } else {
                throw new RuntimeException("发生错误");
            }
        }
    }


    public void destroyAd(String id) {
        adMapper.delete(id);
    }

    public void updateSaleStatus(String id, boolean enabled) {
        adMapper.updateEnable(id, enabled);
    }
}
