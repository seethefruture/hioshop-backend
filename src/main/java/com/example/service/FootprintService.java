package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.mapper.FootprintMapper;
import com.example.mapper.GoodsMapper;
import com.example.po.Footprint;
import com.example.po.Goods;
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

    public void deleteFootprint(Long userId, Long footprintId) {
        footprintMapper.deleteFootprintByUserAndId(userId, footprintId);
    }

    public List<Footprint> listFootprints(Long userId, int page, int size) {
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

    public void addFootprint(Long userId, Long goodsId) {
        if (userId > 0 && goodsId > 0) {
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
