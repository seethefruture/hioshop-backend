package com.example.mapper;

import com.example.po.GoodsGalleryPO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsGalleryMapper {

    @Select(" SELECT * FROM goods_gallery WHERE goods_id = #{goodsId} AND is_delete = 0 ORDER BY sort_order LIMIT 6")
    List<GoodsGalleryPO> selectByGoodsId(@Param("goodsId") String goodsId);

    void insert(GoodsGalleryPO gallery);

    @Select("select * from goods_gallery where goods_id=#{goodsId}")
    List<GoodsGalleryPO> findByGoodsId(@Param("goodsId") String goodsId);

    @Select("select * from goods_gallery where goods_id=#{id}")
    GoodsGalleryPO findById(@Param("id") String id);

    @Delete("delete from goods_gallery where  id=#{id}")
    void delete(@Param("id") String id);

    @Update("update goods_gallery set is_delete=#{isDelete},img_url=#{imgUrl},sort_order=#{sortOrder} where id=#{id}")
    void update(GoodsGalleryPO goodsGalleryPOId);

}
