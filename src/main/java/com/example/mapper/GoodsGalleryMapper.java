package com.example.mapper;

import com.example.vo.GoodsGallery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsGalleryMapper {

    @Select(" SELECT * FROM goods_gallery WHERE goods_id = #{goodsId} AND is_delete = 0 ORDER BY sort_order LIMIT 6")
    List<GoodsGallery> selectByGoodsId(@Param("goodsId") String goodsId);

    void insertGoodsGallery(GoodsGallery gallery);

    List<GoodsGallery> findGoodsGalleryByGoodsId(String goodsId);

    List<GoodsGallery> findByGoodsId(String goodsId);

    void add(GoodsGallery gallery);

    void delete(String id);
}
