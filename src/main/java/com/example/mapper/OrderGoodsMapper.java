package com.example.mapper;

import com.example.po.OrderGoodsPO;
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
    List<OrderGoodsPO> findOrderGoods(@Param("userId") String userId, @Param("orderId") String orderId, @Param("isDelete") Boolean isDelete);

    @Select("select * from order_goods where order_id = #{orderId}")
    List<OrderGoodsPO> findByOrderId(@Param("goodsId") String goodsId);

    List<OrderGoodsPO> findGoodsByOrderId(String id);

    void decrementNumber(String id, int number);

    void incrementNumber(String id, int number);

    void delete(String id);
}
