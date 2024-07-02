package com.example.mapper;

import com.example.po.OrderExpress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderExpressMapper {

    OrderExpress findByOrderId(@Param("orderId") String orderId);

    void update(OrderExpress orderExpress);
}