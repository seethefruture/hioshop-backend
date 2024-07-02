package com.example.mapper;

import com.example.po.Ad;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdMapper {

    @Select("SELECT * FROM ad WHERE end_time < #{currentTime} AND enabled = 1")
    List<Ad> findExpiredAds(@Param("currentTime") long currentTime);

    @Update("UPDATE ad SET enabled = #{enabled} WHERE id = #{id}")
    int update(Ad ad);

    @Select("SELECT link_type, goods_id, image_url, link FROM ad WHERE enabled = 1 AND is_delete = 0 ORDER BY sort_order DESC")
    List<Ad> findEnabledAds();

    List<Ad> findAds(@Param("page") int page, @Param("size") int size);

    void updateSortOrder(@Param("id") int id, @Param("sort") int sort);

    Ad findById(@Param("id") int id);

    void updateAd(@Param("ad") Map<String, Object> ad);

    Ad findAdByGoodsId(@Param("goodsId") int goodsId);

    void addAd(@Param("ad") Map<String, Object> ad);

    List<Map<String, Object>> findAllRelatedGoods();

    void markAdAsDeleted(@Param("id") int id);

    void updateSaleStatus(@Param("id") int id, @Param("enabled") int enabled);
}
