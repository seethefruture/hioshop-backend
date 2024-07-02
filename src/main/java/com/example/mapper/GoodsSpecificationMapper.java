package com.example.mapper;

import com.example.po.GoodsSpecification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsSpecificationMapper {

    @Select("SELECT * FROM goods_specification WHERE goods_id = #{goodsId} and is_delete=0")
    List<GoodsSpecification> selectSpecificationList(@Param("goodsId") Long goodsId);
}
