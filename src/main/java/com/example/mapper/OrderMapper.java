package com.example.mapper;

import com.example.po.Order;
import com.example.po.OrderGoods;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    @Select("SELECT * FROM `order` WHERE order_status IN (101, 801) AND add_time < #{expireTime} AND is_delete = 0")
    List<Order> findExpiredOrders(@Param("expireTime") long expireTime);

    @Select("SELECT * FROM `order` WHERE order_status = 301 AND shipping_time <= #{noConfirmTime} AND shipping_time <> 0 AND is_delete = 0")
    List<Order> findNoConfirmOrders(@Param("noConfirmTime") long noConfirmTime);

    @Update("UPDATE `order` SET order_status = #{orderStatus}, confirm_time = #{confirmTime} WHERE id = #{id}")
    int update(Order order);

    List<Map<String, Object>> listOrders(@Param("userId") Long userId, @Param("showType") int showType, @Param("offset") int offset, @Param("limit") int limit);

    int countOrders(@Param("userId") Long userId, @Param("showType") int showType);

    Map<String, Integer> getOrderCountByStatus(@Param("userId") Long userId);

    Order getOrderDetails(@Param("orderId") Long orderId, @Param("userId") Long userId);

    List<OrderGoods> getOrderGoods(@Param("orderId") Long orderId, @Param("userId") Long userId);

    void cancelOrder(@Param("orderId") Long orderId, @Param("userId") Long userId);

    void deleteOrder(@Param("orderId") Long orderId, @Param("userId") Long userId);

    void confirmOrder(@Param("orderId") Long orderId, @Param("userId") Long userId);

    void completeOrder(@Param("orderId") Long orderId);

    int insertOrder(@Param("orderInfo") Map<String, Object> orderInfo);

    void insertOrderGoods(@Param("goodsItem") Map<String, Object> goodsItem);

    Integer submitOrder(Map<String, Object> order);

    void clearBuyGoods(@Param("userId") String userId);

    Integer updateOrder(@Param("updateInfo") Map<String, Object> updateInfo, @Param("orderId") Long orderId);

    Map<String, Object> getExpressInfo(@Param("orderId") Long orderId);

    @Select("SELECT id, add_time, actual_price, freight_price, offline_pay\n" +
            "        FROM orders\n" +
            "        WHERE user_id = #{userId}\n" +
            "        AND is_delete = #{isDelete}\n" +
            "        AND order_type < 7\n" +
            "        AND order_status IN  #{status}\n" +
            "        ORDER BY add_time DESC\n" +
            "        LIMIT #{page}, #{size}")
    List<Order> findOrders(@Param("userId") Long userId, @Param("isDelete") Boolean isDelete, @Param("status") List<Integer> status, @Param("page") int page, @Param("size") int size);

    @Select("  SELECT add_time FROM orders WHERE id = #{orderId}")
    Long getOrderAddTime(@Param("orderId") Long orderId);

    @Select("SELECT status_text FROM order_status WHERE id = #{orderId}")
    String getOrderStatusText(@Param("orderId") Long orderId);

    @Select(" SELECT handle_option FROM order_handle_option WHERE id = #{orderId}")
    Map<String, Object> getOrderHandleOption(@Param("orderId") Long orderId);

    @Select("SELECT COUNT(id) FROM orders\n" +
            "        WHERE user_id = #{userId}\n" +
            "        AND is_delete = #{isDelete}\n" +
            "        AND order_status IN  #{status}")
    int countOrders(@Param("userId") Long userId, @Param("isDelete") int isDelete, @Param("status") List<Integer> status);

    @Select(" SELECT COUNT(id) FROM orders\n" +
            "        WHERE user_id = #{userId}\n" +
            "        AND is_delete = #{isDelete}\n" +
            "        AND order_status IN (${status})")
    int countOrderByStatus(@Param("userId") Long userId, @Param("isDelete") int isDelete, @Param("status") String status);

    @Select("SELECT * FROM order WHERE id = #{orderId} AND user_id = #{userId}")
    Order findOrderByIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);

    @Select("SELECT * FROM order WHERE id = #{orderId}")
    Order findOrderById(@Param("orderId") Long orderId);

    @Select("SELECT * FROM order_goods WHERE order_id = #{orderId} AND user_id = #{userId} AND is_delete = 0")
    List<Map<String, Object>> findOrderGoodsByOrderIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);

    @Update("UPDATE order SET order_status = #{status} WHERE id = #{orderId}")
    Integer updateOrderStatus(@Param("orderId") Long orderId, @Param("status") int status);

    @Delete("DELETE FROM order WHERE id = #{orderId}")
    void deleteOrderById(@Param("orderId") Long orderId);

    Map<String, Object> getAddress(@Param("addressId") String addressId);

    List<Map<String, Object>> getCheckedGoods(@Param("userId") String userId);

    String generateOrderNumber();

    void updateOrder(@Param("orderId") Long orderId, @Param("order") Order order);


    List<OrderGoods> findOrderGoodsByOrderId(@Param("orderId") Long orderId);

    @Select("SELECT * FROM orders WHERE id = #{orderId}")
    Order findById(@Param("orderId") Long orderId);

    @Update("UPDATE orders SET pay_time = #{payTime} WHERE id = #{orderId}")
    int updatePayTime(@Param("orderId") Long orderId, @Param("payTime") Date payTime);

    @Update("UPDATE orders SET is_delete = 1 WHERE id = #{orderId}")
    int logicDeleteById(@Param("orderId") Long orderId);

    @Select("SELECT COUNT(*) FROM orders WHERE id = #{orderId} AND pay_status = 2")
    int countPaidOrder(@Param("orderId") Long orderId);

    @Update("UPDATE orders SET pay_status = #{payStatus} WHERE id = #{orderId}")
    int updatePayStatus(@Param("orderId") Long orderId, @Param("payStatus") int payStatus);


    @Select("SELECT * FROM orders WHERE order_sn = #{orderSn}")
    Order findByOrderSn(@Param("orderSn") String orderSn);

    @Update({
            "<script>",
            "UPDATE orders SET",
            "<foreach collection='data.keySet()' item='key' separator=','>",
            "${key} = #{data.${key}}",
            "</foreach>",
            "WHERE id = #{orderId}",
            "</script>"
    })
    int updatePayData(@Param("orderId") Long orderId, @Param("data") Map<String, Object> data);
}
