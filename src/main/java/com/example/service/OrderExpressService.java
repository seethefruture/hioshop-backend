package com.example.service;

import com.example.mapper.OrderExpressMapper;
import com.example.po.OrderExpress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderExpressService {

    @Autowired
    private OrderExpressMapper orderExpressMapper;

    public OrderExpress getLatestOrderExpress(Long orderId) {
        OrderExpress returnExpressInfo = new OrderExpress();
        returnExpressInfo.setShipperCode("");
        returnExpressInfo.setShipperName("");
        returnExpressInfo.setLogisticCode("");
        returnExpressInfo.setIsFinish(0);
        returnExpressInfo.setRequestTime(0);
        returnExpressInfo.setTraces("");

        OrderExpress orderExpress = orderExpressMapper.findByOrderId(orderId);
        if (orderExpress == null || orderExpress.getShipperCode() == null || orderExpress.getLogisticCode() == null) {
            return returnExpressInfo;
        }

        returnExpressInfo.setShipperCode(orderExpress.getShipperCode());
        returnExpressInfo.setShipperName(orderExpress.getShipperName());
        returnExpressInfo.setLogisticCode(orderExpress.getLogisticCode());
        returnExpressInfo.setIsFinish(orderExpress.getIsFinish());
        returnExpressInfo.setRequestTime(orderExpress.getRequestTime() * 1000);
        returnExpressInfo.setTraces(orderExpress.getTraces() == null ? "" : orderExpress.getTraces());

        if (orderExpress.getIsFinish() == 1) {
            return returnExpressInfo;
        }


        // 假设 queryExpress 方法返回的是 latestExpressInfo 对象
//        Object latestExpressInfo = queryExpress(orderExpress.getShipperCode(), orderExpress.getLogisticCode());

        long nowTime = System.currentTimeMillis() / 1000;
        OrderExpress updateData = new OrderExpress();
        updateData.setRequestTime(nowTime);
        updateData.setUpdateTime(nowTime);
        updateData.setRequestCount(1);

//        if (latestExpressInfo.isSuccess()) {
//            returnExpressInfo.setTraces(JSON.toJSONString(latestExpressInfo.getTraces()));
//            returnExpressInfo.setIsFinish(latestExpressInfo.getIsFinish());
//
//            updateData.setTraces(JSON.toJSONString(latestExpressInfo.getTraces()));
//            returnExpressInfo.setRequestTime(nowTime * 1000);
//            updateData.setIsFinish(latestExpressInfo.getIsFinish());
//        }

        orderExpressMapper.update(updateData);
        return returnExpressInfo;
    }
}
