package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.mapper.FootprintMapper;
import com.example.mapper.GoodsMapper;
import com.example.vo.Footprint;
import com.example.vo.Goods;
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

    public List<Footprint> listFootprints(String userId, int page, int size) {
        List<Footprint> footprints = footprintMapper.findFootprintsByUserId(userId, (page - 1) * size, size);
        for (Footprint footprint : footprints) {
            Goods goods = goodsMapper.findById(footprint.getGoodsId());
            footprint.setAddTime(formatAddTime(footprint.getAddTime()));
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
        if (StrUtil.isNotEmpty(userId) && StrUtil.isNotEmpty(goodsId) ) {
            long currentTime = System.currentTimeMillis() / 1000;
            Footprint footprint = footprintMapper.findByUserIdAndGoodsId(goodsId, userId);

            if (footprint == null) {
                footprint = new Footprint();
                footprint.setUserId(userId);
                footprint.setGoodsId(goodsId);
                footprint.setAddTime(String.valueOf(currentTime));
                footprintMapper.insert(footprint);
            } else {
                footprint.setAddTime(String.valueOf(currentTime));
                footprintMapper.update(footprint);
            }
        }
    }
}
