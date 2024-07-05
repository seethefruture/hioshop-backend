package com.example.mapper;

import com.example.po.CartPO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    List<CartPO> getCharts(@Param("userId") String userId, @Param("isFast") Boolean isFast, @Param("productId") String productId);

//    @Update("UPDATE cart SET checked = 0 WHERE product_id = #{productId} AND user_id = #{userId} AND is_delete = 0")
//    void uncheckItem(@Param("productId") String productId, @Param("userId") String userId);

    void updateCart(@Param("cartList") List<CartPO> cartPO);

    void insert(CartPO cartPO);

    void updateAddAgain(@Param("retailPrice") String retailPrice, @Param("checked") Boolean checked, @Param("number") Integer number, @Param("id") String id);

    int deleteCheckedProducts(@Param("userId") String userId);

    void deleteProducts(@Param("userId") String userId, @Param("productIds") List<String> productIds);

    void deleteFastCart(@Param("userId") String userId);

    Integer getCartCountByUserId(@Param("userId") String userId);

    int countNewCarts(@Param("beginTimeStamp") long beginTimeStamp, @Param("endTimeStamp") long endTimeStamp);
}
