package com.example.mapper;

import com.example.po.FootprintPO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FootprintMapper {

    @Delete("DELETE FROM footprint WHERE  id = #{footprintId}")
    void deleteFootprintByUserAndId( @Param("footprintId") String footprintId);

    @Select("SELECT * FROM footprint WHERE user_id = #{userId} LIMIT #{offset}, #{limit}")
    List<FootprintPO> findFootprintsByUserId(@Param("userId") String userId, @Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO footprint (id,user_id, goods_id, add_time)" +
            "        VALUES (#{id},#{userId}, #{goodsId}, #{addTime})")
    int insert(FootprintPO footprintPO);

    @Update("UPDATE footprint SET add_time = #{addTime} WHERE goods_id = #{goodsId} AND user_id = #{userId}")
    void update(FootprintPO footprintPO);

    @Select("SELECT * FROM footprint WHERE goods_id = #{goodsId} AND user_id = #{userId}")
    FootprintPO findByUserIdAndGoodsId(@Param("goodsId") String goodsId, @Param("userId") String userId);
}
