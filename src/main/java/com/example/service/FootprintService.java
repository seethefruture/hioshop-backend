package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.mapper.FootprintMapper;
import com.example.mapper.GoodsMapper;
import com.example.po.FootprintPO;
import com.example.po.GoodsPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FootprintService {

    @Autowired
    private FootprintMapper footprintMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    public void deleteFootprint(String footprintId) {
        footprintMapper.deleteFootprintByUserAndId(footprintId);
    }

    public List<FootprintPO> listFootprints(String userId, int page, int size) {
        List<FootprintPO> footprints = footprintMapper.findFootprintsByUserId(userId, (page - 1) * size, size);
        for (FootprintPO footprint : footprints) {
            GoodsPO goods = goodsMapper.findById(footprint.getGoodsId());
            footprint.setGoods(goods);
        }
        return footprints;
    }

    private String formatAddTime(String addTime) {
        LocalDate today = LocalDate.now();
        LocalDate addDate = LocalDate.ofEpochDay(DateUtil.parse(addTime, "yyyy-MM-dd").getTime());
        if (addDate.equals(today)) {
            return "今天";
        } else {
            return addDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }

    public void addFootprint(String userId, String goodsId) {
        if (StrUtil.isNotEmpty(userId) && StrUtil.isNotEmpty(goodsId)) {
            long currentTime = System.currentTimeMillis() / 1000;
            FootprintPO footprint = footprintMapper.findByUserIdAndGoodsId(goodsId, userId);

            if (footprint == null) {
                footprint = new FootprintPO();
                footprint.setUserId(userId);
                footprint.setGoodsId(goodsId);
                footprintMapper.insert(footprint);
            } else {
                footprintMapper.update(footprint);
            }
        }
    }
}
