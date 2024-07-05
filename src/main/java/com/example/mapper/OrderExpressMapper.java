package com.example.mapper;

import com.example.po.OrderExpressPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderExpressMapper {

    @Select("select * from order_express where order_id =#{orderId}")
    OrderExpressPO findByOrderId(@Param("orderId") String orderId);

    @Update("update order_express set request_time=#{requestTime},update_time=#{updateTime},request_count=#{requestCount} where order_id=#{orderId}")
    void update(@Param("requestTime") long requestTime, @Param("updateTime") long updateTime, @Param("requestCount") int requestCount, @Param("orderId") String orderId);

    @Select("select * from order_express where logistic_code=#{logisticCode}")
    OrderExpressPO findByLogisticCode(@Param("logisticCode") String logisticCode);
}