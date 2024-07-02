package com.example.mapper;

import com.example.po.Ad;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdMapper {

    List<Ad> findExpiredAds();

    List<Ad> findExpiredAdsPage(@Param("page") int page, @Param("size") int size);

    int updateEnable(@Param("id") String id, @Param("enable") Boolean enable);

    void updateSortOrder(@Param("id") String id, @Param("sort") int sort);

    Ad findById(@Param("id") String id);

    void updateEndTime(@Param("id") String id, @Param("endTime") Long endTime);

    Ad findAdByGoodsId(@Param("goodsId") int goodsId);

    Integer insert(Ad ad);

    void delete(@Param("id") String id);
}
