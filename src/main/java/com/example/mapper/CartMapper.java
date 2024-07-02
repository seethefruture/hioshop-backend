package com.example.mapper;

import com.example.po.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {
    List<Cart> getCharts(@Param("userId") String userId, @Param("isFast") Boolean isFast, @Param("productId") String productId);

    @Update("UPDATE cart SET checked = 0 WHERE product_id = #{productId} AND user_id = #{userId} AND is_delete = 0")
    void uncheckItem(@Param("productId") String productId, @Param("userId") String userId);

    void updateCart(@Param("cartList") List<Cart> cart);


    @Insert("INSERT INTO cart (goods_id, product_id, goods_sn, goods_name, goods_weight, list_pic_url, number, user_id, retail_price, add_price, goods_specification_name_value, checked, add_time) VALUES (#{goodsId}, #{productId}, #{goodsSn}, #{goodsName}, #{goodsWeight}, #{listPicUrl}, #{number}, #{userId}, #{retailPrice}, #{addPrice}, #{goodsSpecificationNameValue}, #{checked}, #{addTime})")
    void insert(Cart cart);

    @Update("UPDATE cart SET retail_price = #{cart.retailPrice}, checked = #{cart.checked}, number = #{cart.number} WHERE id = #{cart.id}")
    void update(Cart cart);

    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND checked = 1 AND is_delete = 0")
    List<Cart> selectCheckedGoodsList(@Param("userId") String userId);

    @Select(" UPDATE cart SET is_delete = 1 WHERE user_id = #{userId} AND checked = 1 AND is_delete = 0")
    int deleteAllProducts(@Param("userId") String userId);

    void deleteProducts(@Param("userId") String userId, @Param("productIds") List<String> productIds);


    @Update("UPDATE cart SET is_delete = 1 WHERE user_id = #{userId} AND is_delete = 0 AND is_fast = 1")
    void updateFastCart(@Param("userId") String userId);


    Integer getCartCountByUserId(@Param("userId") String userId);
}
