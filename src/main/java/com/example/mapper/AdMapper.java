package com.example.mapper;

import com.example.po.AdPO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdMapper {

    List<AdPO> findExpiredAds();

    List<AdPO> findExpiredAdsPage(@Param("page") int page, @Param("size") int size);

    int updateEnable(@Param("id") String id, @Param("enable") Boolean enable);

    void updateSortOrder(@Param("id") String id, @Param("sort") int sort);

    AdPO findById(@Param("id") String id);

    void updateEndTime(@Param("id") String id, @Param("endTime") Long endTime);

    AdPO findAdByGoodsId(@Param("goodsId") String goodsId);

    Integer insert(AdPO adPO);

    void delete(@Param("id") String id);
}
