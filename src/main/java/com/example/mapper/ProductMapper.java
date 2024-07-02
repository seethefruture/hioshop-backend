package com.example.mapper;

import com.example.po.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM product WHERE id = #{id} AND is_delete = 0")
    Product findById(@Param("id") Long id);

    @Select("SELECT * FROM product WHERE id in #{id} AND is_delete = 0")
    List<Product> findByIdList(@Param("idList") List<Long> idList);

    @Select("SELECT value FROM goods_specification WHERE goods_id = #{goodsId} AND is_delete = 0 AND id IN (${ids})")
    List<String> findSpecificationValues(@Param("goodsId") Long goodsId, @Param("ids") String ids);

    void decrementGoodsNumber(@Param("id") Long id, @Param("number") int number);

    void incrementSellVolume(@Param("id") Long id, @Param("number") int number);

    @Select(" SELECT * FROM product WHERE goods_specification_ids = #{goodsSpecificationIds} and is_delete=#{isDelete}")
    Product findByGoodsSpecificationIdsAndIsDelete(@Param("goodsSpecificationIds") Long goodsSpecificationIds, @Param("isDelete") int isDelete);

    @Select(" SELECT * FROM product WHERE goods_id = #{goodsId} and is_delete=0")
    List<Product> selectProductList(@Param("goodsId") Long goodsId);

    @Select(" SELECT * FROM product WHERE goods_id = #{goodsId} and goods_specification_ids=#{goodsSpecificationIds} and is_delete=#{isDelete}")
    Product findProductByGoodsDetail(Long goodsId, Long goodsSpecificationIds, int isDelete);
}
