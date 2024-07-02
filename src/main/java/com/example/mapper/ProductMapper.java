package com.example.mapper;

import com.example.po.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM product WHERE id = #{id} AND is_delete = 0")
    Product findById(@Param("id") String id);

    @Select("SELECT * FROM product WHERE id in #{id} AND is_delete = 0")
    List<Product> findByIdList(@Param("idList") List<String> idList);

    @Select("SELECT value FROM goods_specification WHERE goods_id = #{goodsId} AND is_delete = 0 AND id IN (${ids})")
    List<String> findSpecificationValues(@Param("goodsId") String goodsId, @Param("ids") String ids);

    void decrementGoodsNumber(@Param("id") String id, @Param("number") int number);

    void incrementSellVolume(@Param("id") String id, @Param("number") int number);

    @Select(" SELECT * FROM product WHERE goods_specification_ids = #{goodsSpecificationIds} and is_delete=#{isDelete}")
    Product findByGoodsSpecificationIdsAndIsDelete(@Param("goodsSpecificationIds") String goodsSpecificationIds, @Param("isDelete") int isDelete);

    @Select(" SELECT * FROM product WHERE goods_id = #{goodsId} and is_delete=0")
    List<Product> selectProductList(@Param("goodsId") String goodsId);

    @Select(" SELECT * FROM product WHERE goods_id = #{goodsId} and goods_specification_ids=#{goodsSpecificationIds} and is_delete=#{isDelete}")
    Product findProductByGoodsDetail(String goodsId, String goodsSpecificationIds, int isDelete);
}
