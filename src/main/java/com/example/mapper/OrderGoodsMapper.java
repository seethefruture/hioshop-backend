package com.example.mapper;

import com.example.po.OrderGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderGoodsMapper {

    @Select(" SELECT id, list_pic_url, number\n" +
            "        FROM order_goods\n" +
            "        WHERE user_id = #{userId}\n" +
            "        AND order_id = #{orderId}\n" +
            "        AND is_delete = #{isDelete}")
    List<OrderGoods> findOrderGoods(@Param("userId") Long userId, @Param("orderId") Long orderId, @Param("isDelete") Boolean isDelete);
}
