package com.example.mapper;

import com.example.vo.Footprint;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FootprintMapper {

    @Delete("DELETE FROM footprint WHERE user_id = #{userId} AND id = #{footprintId}")
    void deleteFootprintByUserAndId(@Param("userId") String userId, @Param("footprintId") String footprintId);

    @Select("SELECT * FROM footprint WHERE user_id = #{userId} LIMIT #{offset}, #{limit}")
    List<Footprint> findFootprintsByUserId(@Param("userId") String userId, @Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO footprint (user_id, goods_id, add_time)\n" +
            "        VALUES (#{userId}, #{goodsId}, #{addTime})")
    int insert(Footprint footprint);

    @Update("UPDATE footprint SET add_time = #{addTime} WHERE goods_id = #{goodsId} AND user_id = #{userId}")
    void update(Footprint footprint);
    @Select("SELECT * FROM footprint WHERE goods_id = #{goodsId} AND user_id = #{userId}")
    Footprint findByUserIdAndGoodsId(@Param("goodsId") String goodsId, @Param("userId") String userId);
}
