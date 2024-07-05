package com.example.service;

import com.example.mapper.OrderExpressMapper;
import com.example.po.OrderExpressPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderExpressService {

    @Autowired
    private OrderExpressMapper orderExpressMapper;

    public OrderExpressPO getLatestOrderExpress(String orderId) {
        OrderExpressPO returnExpressInfo = new OrderExpressPO();
        returnExpressInfo.setShipperCode("");
        returnExpressInfo.setShipperName("");
        returnExpressInfo.setLogisticCode("");
        returnExpressInfo.setFinish(false);
        returnExpressInfo.setRequestTime(0);
        returnExpressInfo.setTraces("");

        OrderExpressPO orderExpress = orderExpressMapper.findByOrderId(orderId);
        if (orderExpress == null || orderExpress.getShipperCode() == null || orderExpress.getLogisticCode() == null) {
            return returnExpressInfo;
        }

        returnExpressInfo.setShipperCode(orderExpress.getShipperCode());
        returnExpressInfo.setShipperName(orderExpress.getShipperName());
        returnExpressInfo.setLogisticCode(orderExpress.getLogisticCode());
        returnExpressInfo.setFinish(orderExpress.isFinish());
        returnExpressInfo.setRequestTime(orderExpress.getRequestTime() * 1000);
        returnExpressInfo.setTraces(orderExpress.getTraces() == null ? "" : orderExpress.getTraces());

        if (orderExpress.isFinish()) {
            return returnExpressInfo;
        }


        // 假设 queryExpress 方法返回的是 latestExpressInfo 对象
//        Object latestExpressInfo = queryExpress(orderExpress.getShipperCode(), orderExpress.getLogisticCode());

        long nowTime = System.currentTimeMillis() / 1000;

//        if (latestExpressInfo.isSuccess()) {
//            returnExpressInfo.setTraces(JSON.toJSONString(latestExpressInfo.getTraces()));
//            returnExpressInfo.setIsFinish(latestExpressInfo.getIsFinish());
//
//            updateData.setTraces(JSON.toJSONString(latestExpressInfo.getTraces()));
//            returnExpressInfo.setRequestTime(nowTime * 1000);
//            updateData.setIsFinish(latestExpressInfo.getIsFinish());
//        }

        orderExpressMapper.update(nowTime, nowTime, 1, orderId);
        return returnExpressInfo;
    }
}
