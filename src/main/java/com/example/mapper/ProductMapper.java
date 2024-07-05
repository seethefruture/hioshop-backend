package com.example.mapper;

import com.example.po.ProductPO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM product WHERE id = #{id} AND is_delete = 0")
    ProductPO findById(@Param("id") String id);

    @Select("SELECT * FROM product WHERE id in #{id} AND is_delete = 0")
    List<ProductPO> findByIdList(@Param("idList") List<String> idList);

    @Select("SELECT value FROM goods_specification WHERE goods_id = #{goodsId} AND is_delete = 0 AND id IN (${ids})")
    List<String> findSpecificationValues(@Param("goodsId") String goodsId, @Param("ids") String ids);

    void decrementGoodsNumber(@Param("id") String id, @Param("number") int number);

    void incrementSellVolume(@Param("id") String id, @Param("number") int number);

    @Select(" SELECT * FROM product WHERE goods_specification_ids = #{goodsSpecificationIds} and is_delete=#{isDelete}")
    ProductPO findByGoodsSpecificationIdsAndIsDelete(@Param("goodsSpecificationIds") String goodsSpecificationIds, @Param("isDelete") int isDelete);

    @Select(" SELECT * FROM product WHERE goods_id = #{goodsId} and is_delete=0")
    List<ProductPO> selectProductList(@Param("goodsId") String goodsId);

    @Select(" SELECT * FROM product WHERE goods_id = #{goodsId} and is_delete=0 and is_on_sale=1")
    List<ProductPO> findOnSaleProductsByGoodsId(@Param("goodsId") String goodsId);

    @Select(" SELECT * FROM product WHERE goods_id = #{goodsId} and goods_specification_ids=#{goodsSpecificationIds} and is_delete=#{isDelete}")
    ProductPO findProductByGoodsDetail(@Param("goodsId") String goodsId, @Param("goodsSpecificationIds") String goodsSpecificationIds, @Param("isDelete") int isDelete);

    List<ProductPO> findProductsByGoodsId(String id);

    void updateGoodsNumberBySn(String goodsSn, int goodsNumber);

    void updateSaleStatusByGoodsId(String id, Boolean saleStatus);

    int sumGoodsNumberByGoodsId(String id);

    void updateProductStatus(String id, Integer status);

    void updateCartProductStatus(String id, Integer status);

    void updatePrice(Map<String, Object> data);

    Boolean isSkuUnique(Map<String, Object> info);

    @Update("update product set is_delete = 1 where goods_id=#{goodsId}")
    void deleteByGoodsId(@Param("goodsId") String goodsId);

    void insert(ProductPO productPO);

}
