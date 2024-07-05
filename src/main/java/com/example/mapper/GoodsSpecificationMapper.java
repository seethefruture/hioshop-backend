package com.example.mapper;

import com.example.po.GoodsSpecificationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsSpecificationMapper {

    @Select("SELECT * FROM goods_specification WHERE goods_id = #{goodsId} and is_delete=0")
    List<GoodsSpecificationPO> selectSpecificationList(@Param("goodsId") String goodsId);

    @Select("select * from goods_specification")
    List<GoodsSpecificationPO> findAll();

    @Update("update goods_specification set value=#{value}, specification_id=#{specification_id},is_delete=0 where id=#{id}")
    void updateGoodsSpecification(@Param("value") String value, @Param("specification_id") String specification_id, @Param("id") String id);

    void insert(GoodsSpecificationPO goodsSpecificationPO);

    @Update("update goods_specification set is_delete = 1 where  goods_id=#{goodsId};")
    void deleteByGoodsId(@Param("goodsId") String goodsId);
}
