package com.example.mapper;

import com.example.po.OrderGoodsPO;
import com.example.po.OrderPO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    @Select("SELECT * FROM `orders` WHERE order_status IN (101, 801) AND add_time < #{expireTime} AND is_delete = 0")
    List<OrderPO> findExpiredOrders(@Param("expireTime") long expireTime);

    @Select("SELECT * FROM `orders` WHERE order_status = 301 AND shipping_time <= #{noConfirmTime} AND shipping_time <> 0 AND is_delete = 0")
    List<OrderPO> findNoConfirmOrders(@Param("noConfirmTime") long noConfirmTime);

    @Update("UPDATE `orders` SET order_status = #{orderStatus}, confirm_time = #{confirmTime} WHERE id = #{id}")
    int update(OrderPO order);

    List<Map<String, Object>> listOrders(@Param("userId") String userId, @Param("showType") int showType, @Param("offset") int offset, @Param("limit") int limit);

    int countOrders(@Param("userId") String userId, @Param("showType") int showType);

    Map<String, Integer> getOrderCountByStatus(@Param("userId") String userId);

    OrderPO getOrderDetails(@Param("orderId") String orderId, @Param("userId") String userId);

    List<OrderGoodsPO> getOrderGoods(@Param("orderId") String orderId, @Param("userId") String userId);

    void cancelOrder(@Param("orderId") String orderId, @Param("userId") String userId);

    void deleteOrder(@Param("orderId") String orderId, @Param("userId") String userId);

    void confirmOrder(@Param("orderId") String orderId, @Param("userId") String userId);

    void completeOrder(@Param("orderId") String orderId);

    int insertOrder(@Param("orderInfo") Map<String, Object> orderInfo);

    void insertOrderGoods(@Param("goodsItem") Map<String, Object> goodsItem);

    Integer submitOrder(Map<String, Object> order);

    void clearBuyGoods(@Param("userId") String userId);

    Integer updateOrder(@Param("updateInfo") Map<String, Object> updateInfo, @Param("orderId") String orderId);

    Map<String, Object> getExpressInfo(@Param("orderId") String orderId);

    @Select("SELECT id, add_time, actual_price, freight_price, offline_pay\n" +
            "        FROM orders\n" +
            "        WHERE user_id = #{userId}\n" +
            "        AND is_delete = #{isDelete}\n" +
            "        AND order_type < 7\n" +
            "        AND order_status IN  #{status}\n" +
            "        ORDER BY add_time DESC\n" +
            "        LIMIT #{page}, #{size}")
    List<OrderPO> findOrders(@Param("userId") String userId, @Param("isDelete") Boolean isDelete, @Param("status") List<Integer> status, @Param("page") int page, @Param("size") int size);

    @Select("  SELECT add_time FROM orders WHERE id = #{orderId}")
    Long getOrderAddTime(@Param("orderId") String orderId);

    @Select("SELECT status_text FROM order_status WHERE id = #{orderId}")
    String getOrderStatusText(@Param("orderId") String orderId);

    @Select(" SELECT handle_option FROM order_handle_option WHERE id = #{orderId}")
    Map<String, Object> getOrderHandleOption(@Param("orderId") String orderId);

    @Select("SELECT COUNT(id) FROM orders\n" +
            "        WHERE user_id = #{userId}\n" +
            "        AND is_delete = #{isDelete}\n" +
            "        AND order_status IN  #{status}")
    int countOrders(@Param("userId") String userId, @Param("isDelete") Boolean isDelete, @Param("status") List<Integer> status);

    @Select(" SELECT COUNT(id) FROM orders\n" +
            "        WHERE user_id = #{userId}\n" +
            "        AND is_delete = #{isDelete}\n" +
            "        AND order_status IN (${status})")
    int countOrderByStatus(@Param("userId") String userId, @Param("isDelete") Boolean isDelete, @Param("status") String status);

    @Select("SELECT * FROM order WHERE id = #{orderId} AND user_id = #{userId}")
    OrderPO findOrderByIdAndUserId(@Param("orderId") String orderId, @Param("userId") String userId);

    @Select("SELECT * FROM order WHERE id = #{orderId}")
    OrderPO findOrderById(@Param("orderId") String orderId);

    @Select("SELECT * FROM order_goods WHERE order_id = #{orderId} AND user_id = #{userId} AND is_delete = 0")
    List<Map<String, Object>> findOrderGoodsByOrderIdAndUserId(@Param("orderId") String orderId, @Param("userId") String userId);

    @Update("UPDATE order SET order_status = #{status} WHERE id = #{orderId}")
    Integer updateOrderStatus(@Param("orderId") String orderId, @Param("status") int status);

    @Delete("DELETE FROM order WHERE id = #{orderId}")
    void deleteOrderById(@Param("orderId") String orderId);

    Map<String, Object> getAddress(@Param("addressId") String addressId);

    List<Map<String, Object>> getCheckedGoods(@Param("userId") String userId);

    String generateOrderNumber();

    void updateOrder(@Param("orderId") String orderId, @Param("order") OrderPO order);


    List<OrderGoodsPO> findOrderGoodsByOrderId(@Param("orderId") String orderId);

    @Select("SELECT * FROM orders WHERE id = #{orderId}")
    OrderPO findById(@Param("orderId") String orderId);

    @Update("UPDATE orders SET pay_time = #{payTime} WHERE id = #{orderId}")
    int updatePayTime(@Param("orderId") String orderId, @Param("payTime") Date payTime);

    @Update("UPDATE orders SET is_delete = 1 WHERE id = #{orderId}")
    int logicDeleteById(@Param("orderId") String orderId);

    @Select("SELECT COUNT(*) FROM orders WHERE id = #{orderId} AND pay_status = 2")
    int countPaidOrder(@Param("orderId") String orderId);

    @Update("UPDATE orders SET pay_status = #{payStatus} WHERE id = #{orderId}")
    int updatePayStatus(@Param("orderId") String orderId, @Param("payStatus") int payStatus);


    @Select("SELECT * FROM orders WHERE order_sn = #{orderSn}")
    OrderPO findByOrderSn(@Param("orderSn") String orderSn);

    @Update({
            "<script>",
            "UPDATE orders SET",
            "<foreach collection='data.keySet()' item='key' separator=','>",
            "${key} = #{data.${key}}",
            "</foreach>",
            "WHERE id = #{orderId}",
            "</script>"
    })
    int updatePayData(@Param("orderId") String orderId, @Param("data") Map<String, Object> data);

    int sumOrderPrice(@Param("beginTimeStamp") long beginTimeStamp, @Param("endTimeStamp") long endTimeStamp);

    int countNewOrders(@Param("beginTimeStamp") long beginTimeStamp, @Param("endTimeStamp") long endTimeStamp);

    int countPaidOrders(@Param("beginTimeStamp") long beginTimeStamp, @Param("endTimeStamp") long endTimeStamp);

    int sumPaidOrderPrice(@Param("beginTimeStamp") long beginTimeStamp, @Param("endTimeStamp") long endTimeStamp);

    int countToDelivery();

    @Select("select * from orders where order_sn=#{orderSn} and consignee=#{consignee} and order_status=#{status} LIMIT #{page}, #{size}")
    List<OrderPO> findOrdersInfo(@Param("orderSn") String orderSn, @Param("consignee") String consignee, @Param("status") String status, @Param("page") int page, @Param("size") int size);

    boolean getAutoStatus();

    List<OrderPO> findDeliveryOrders(int page, int size, String status);

    String getOrderBtnText(String id);

    List<Map<String, Object>> getGoodsSpecification(Long goodsId);

    void decrementOrderPrice(String orderId, double changePrice);

    void incrementOrderPrice(String orderId, double changePrice);

    void updateOrderSn(String orderId, String orderSn);

    boolean updateAdminMemo(String id, String text);
}
