package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.example.po.Address;
import com.example.po.Order;
import com.example.po.OrderExpress;
import com.example.po.OrderGoods;
import com.example.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private RegionMapper regionMapper;

    public Map<String, Object> listAllOrders(String userId, int showType, int page, int size) {
        List<Integer> status = getOrderStatus(showType);
        List<Order> orderList = orderMapper.findOrders(userId, false, status, page, size);

        for (Order order : orderList) {
            List<OrderGoods> goodsList = orderGoodsMapper.findOrderGoods(userId, order.getId(), false);
            int goodsCount = 0;
            for (OrderGoods goods : goodsList) {
                goodsCount += goods.getNumber();
            }
            order.setGoodsList(goodsList);
            order.setGoodsCount(goodsCount);
            order.setOrderStatusText(getOrderStatusText(order.getOrderStatus()));
            order.setHandleOption(getOrderHandleOption(order.getOrderStatus()));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", orderList);
        return result;
    }

    public Map<String, Object> countAction(String userId, int showType) {
        List<Integer> status = getOrderStatus(showType);
        int allCount = orderMapper.countOrders(userId, false, status);
        Map<String, Object> result = new HashMap<>();
        result.put("allCount", allCount);
        return result;
    }

    public Map<String, Object> orderCountAction(String userId) {
        if (StrUtil.isNotEmpty(userId)) {
            int toPay = orderMapper.countOrderByStatus(userId, false, "101,801");
            int toDelivery = orderMapper.countOrderByStatus(userId, false, "300");
            int toReceive = orderMapper.countOrderByStatus(userId, false, "301");

            Map<String, Object> newStatus = new HashMap<>();
            newStatus.put("toPay", toPay);
            newStatus.put("toDelivery", toDelivery);
            newStatus.put("toReceive", toReceive);

            return newStatus;
        }
        return null;
    }

    public Map<String, Object> getOrderDetail(String orderId, String userId) {
        Order order = orderMapper.findOrderByIdAndUserId(orderId, userId);
        long currentTime = System.currentTimeMillis() / 1000;
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        Map<String, String> manyRegionNameMap = regionMapper.getManyRegionNameById(Arrays.asList(order.getProvince(), order.getCity(), order.getDistrict()));
        order.setProvinceName(manyRegionNameMap.get(order.getProvince()));
        order.setCityName(manyRegionNameMap.get(order.getCity()));
        order.setDistrictName(manyRegionNameMap.get(order.getDistrict()));
        order.setFullRegion(order.getProvinceName() + order.getCityName() + order.getDistrictName());
        order.setPostscript(new String(Base64.getDecoder().decode(order.getPostscript()), StandardCharsets.UTF_8));

        List<OrderGoods> orderGoods = orderGoodsMapper.findOrderGoods(userId, orderId, null);
        int goodsCount = orderGoods.stream().mapToInt(OrderGoods::getNumber).sum();
        order.setOrderStatusText(orderMapper.getOrderStatusText(orderId));
        if (order.getOrderStatus() == 101 || order.getOrderStatus() == 801) {
            order.setFinalPayTime(order.getAddTime() + 24 * 60 * 60);
            if (order.getFinalPayTime() < currentTime) {
                orderMapper.updateOrderStatus(orderId, 102);
            }
        }
        Map<String, Object> handleOption = orderMapper.getOrderHandleOption(orderId);
        Map<String, Boolean> textCode = getOrderTextCode(order.getOrderStatus());

        Map<String, Object> response = new HashMap<>();
        response.put("orderInfo", order);
        response.put("orderGoods", orderGoods);
        response.put("handleOption", handleOption);
        response.put("textCode", textCode);
        response.put("goodsCount", goodsCount);

        return response;
    }


    public Map<String, Object> getOrderGoods(String orderId, String userId) {
        List<Map<String, Object>> orderGoods = orderMapper.findOrderGoodsByOrderIdAndUserId(orderId, userId);

        int goodsCount = orderGoods.stream().mapToInt(g -> (int) g.get("number")).sum();

        Map<String, Object> response = new HashMap<>();
        response.put("orderGoods", orderGoods);
        response.put("goodsCount", goodsCount);

        return response;
    }

    public Map<String, Object> cancelOrder(String orderId, String userId) {
        Order order = orderMapper.findOrderByIdAndUserId(orderId, userId);
        if (order == null || order.getHandleOption().get("") != null) {
            throw new RuntimeException("订单不能取消");
        }
        orderMapper.updateOrderStatus(orderId, 102);
        // TODO goods,product增加库存：goods_number
        return Collections.singletonMap("success", true);
    }

    public Map<String, Object> deleteOrder(String orderId) {
        Order order = orderMapper.findOrderById(orderId);
        if (order == null || order.getHandleOption().get("") != null) {
            throw new RuntimeException("订单不能删除");
        }
        orderMapper.deleteOrderById(orderId);
        return Collections.singletonMap("success", true);
    }

    public Map<String, Object> confirmOrder(String orderId) throws Exception {
        Map<String, Object> handleOption = orderMapper.getOrderHandleOption(orderId);
        if (!(Boolean) handleOption.get("confirm")) {
            throw new Exception("订单不能确认");
        }

        long currentTime = System.currentTimeMillis() / 1000;
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("order_status", 401);
        updateInfo.put("confirm_time", currentTime);

        int updateCount = orderMapper.updateOrder(updateInfo, orderId);
        if (updateCount == 0) {
            throw new Exception("订单确认失败");
        }

        return updateInfo;
    }

    public Map<String, Object> completeOrder(String orderId) throws Exception {
        long currentTime = System.currentTimeMillis() / 1000;
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("order_status", 401);
        updateInfo.put("dealdone_time", currentTime);

        int updateCount = orderMapper.updateOrder(updateInfo, orderId);
        if (updateCount == 0) {
            throw new Exception("订单完成失败");
        }

        return updateInfo;
    }

    public Map<String, Object> submitOrder(Map<String, Object> payload) throws Exception {
        String userId = (String) payload.get("userId");
        String addressId = (String) payload.get("addressId");
        double freightPrice = (Double) payload.get("freightPrice");
        boolean offlinePay = (Boolean) payload.get("offlinePay");
        String postscript = (String) payload.get("postscript");

        Map<String, Object> checkedAddress = orderMapper.getAddress(addressId);
        if (checkedAddress == null) {
            throw new Exception("请选择收货地址");
        }

        List<Map<String, Object>> checkedGoodsList = orderMapper.getCheckedGoods(userId);
        if (checkedGoodsList == null || checkedGoodsList.isEmpty()) {
            throw new Exception("请选择商品");
        }

        double goodsTotalPrice = 0.0;
        for (Map<String, Object> item : checkedGoodsList) {
            double itemPrice = (Double) item.get("retail_price");
            int itemNumber = (Integer) item.get("number");
            goodsTotalPrice += itemPrice * itemNumber;
        }

        double orderTotalPrice = goodsTotalPrice + freightPrice;

        long currentTime = System.currentTimeMillis() / 1000;
        String orderSn = orderMapper.generateOrderNumber();

        Map<String, Object> orderInfo = new HashMap<>();
        orderInfo.put("order_sn", orderSn);
        orderInfo.put("user_id", userId);
        orderInfo.put("consignee", checkedAddress.get("name"));
        orderInfo.put("mobile", checkedAddress.get("mobile"));
        orderInfo.put("province", checkedAddress.get("province_id"));
        orderInfo.put("city", checkedAddress.get("city_id"));
        orderInfo.put("district", checkedAddress.get("district_id"));
        orderInfo.put("address", checkedAddress.get("address"));
        orderInfo.put("order_status", 101);
        orderInfo.put("freight_price", freightPrice);
        orderInfo.put("postscript", Base64.getEncoder().encodeToString(postscript.getBytes(StandardCharsets.UTF_8)));
        orderInfo.put("add_time", currentTime);
        orderInfo.put("goods_price", goodsTotalPrice);
        orderInfo.put("order_price", orderTotalPrice);
        orderInfo.put("actual_price", orderTotalPrice);
        orderInfo.put("change_price", orderTotalPrice);
        orderInfo.put("offline_pay", offlinePay);

        int orderId = orderMapper.insertOrder(orderInfo);
        if (orderId == 0) {
            throw new Exception("订单提交失败");
        }

        for (Map<String, Object> goodsItem : checkedGoodsList) {
            goodsItem.put("order_id", orderId);
            goodsItem.put("user_id", userId);
            orderMapper.insertOrderGoods(goodsItem);
        }

        orderMapper.clearBuyGoods(userId);

        orderInfo.put("id", orderId);
        return orderInfo;
    }

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private OrderExpressMapper orderExpressMapper;
    @Autowired
    private RestTemplate restTemplate;

    public Map<String, Object> updateOrder(String addressId, String orderId) {
        Address updateAddress = addressMapper.findById(addressId);
        Order orderInfo = new Order();
        orderInfo.setConsignee(updateAddress.getName());
        orderInfo.setMobile(updateAddress.getMobile());
        orderInfo.setProvince(updateAddress.getProvinceId());
        orderInfo.setCity(updateAddress.getCityId());
        orderInfo.setDistrict(updateAddress.getDistrictId());
        orderInfo.setAddress(updateAddress.getAddress());

        orderMapper.updateOrder(orderId, orderInfo);
        Map<String, Object> result = new HashMap<>();
        result.put("updateInfo", orderInfo);
        return result;
    }

    public Map<String, Object> getExpressInfo(String orderId) throws Exception {
        long currentTime = System.currentTimeMillis() / 1000;
        OrderExpress info = orderExpressMapper.findByOrderId(orderId);

        if (info == null) {
            throw new Exception("暂无物流信息");
        }

        long updateTime = info.getUpdateTime();
        long com = (currentTime - updateTime) / 60;
        int isFinish = info.getIsFinish();

        if (isFinish == 1 || (updateTime != 0 && com < 20)) {
            return createExpressResponse(info);
        } else {
            String shipperCode = info.getShipperCode();
            String expressNo = info.getLogisticCode();
            OrderExpress lastExpressInfo = getExpressInfoFromAPI(shipperCode, expressNo);
            lastExpressInfo.setUpdateTime(System.currentTimeMillis() / 1000);
            orderExpressMapper.update(lastExpressInfo);
            return createExpressResponse(lastExpressInfo);
        }
    }

    private Map<String, Object> createExpressResponse(OrderExpress info) {
        Map<String, Object> expressInfo = new HashMap<>();
        expressInfo.put("express_status", getDeliveryStatus(info.getExpressStatus()));
        expressInfo.put("is_finish", info.getIsFinish());
        expressInfo.put("traces", JSON.parse(info.getTraces()));
        expressInfo.put("update_time", info.getUpdateTime());
        return expressInfo;
    }

    private OrderExpress getExpressInfoFromAPI(String shipperCode, String expressNo) {
        String url = "http://wuliu.market.alicloudapi.com/kdi?no=" + expressNo + "&type=" + shipperCode;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        Map<String, Object> sessionData = JSON.parseObject(response.getBody(), Map.class);
        return JSON.parseObject(sessionData.get("result").toString(), OrderExpress.class);
    }

    private String getDeliveryStatus(int status) {
        switch (status) {
            case 0:
                return "快递收件(揽件)";
            case 1:
                return "在途中";
            case 2:
                return "正在派件";
            case 3:
                return "已签收";
            case 4:
                return "派送失败(无法联系到收件人或客户要求择日派送，地址不详或手机号不清)";
            case 5:
                return "疑难件(收件人拒绝签收，地址有误或不能送达派送区域，收费等原因无法正常派送)";
            case 6:
                return "退件签收";
            default:
                return "未知状态";
        }
    }

    /**
     * 生成订单的编号order_sn
     *
     * @return String
     */
    public String generateOrderNumber() {
        Date date = new Date();
        return String.format("%d%02d%02d%02d%02d%02d%d",
                date.getYear() + 1900,
                date.getMonth() + 1,
                date.getDate(),
                date.getHours(),
                date.getMinutes(),
                date.getSeconds(),
                ThreadLocalRandom.current().nextInt(100000, 999999));
    }


    /**
     * 获取订单可操作的选项
     *
     * @return Map<String, Boolean>
     */
    public Map<String, Boolean> getOrderHandleOption(Integer orderStatus) {
        Map<String, Boolean> handleOption = new HashMap<>();
        handleOption.put("cancel", false);
        handleOption.put("delete", false);
        handleOption.put("pay", false);
        handleOption.put("confirm", false);
        handleOption.put("cancel_refund", false);

//        Order orderInfo = orderMapper.findById(orderId);
        if (orderStatus == 101 || orderStatus == 801) {
            handleOption.put("cancel", true);
            handleOption.put("pay", true);
        }
        if (orderStatus == 102 || orderStatus == 103) {
            handleOption.put("delete", true);
        }
        if (orderStatus == 201) {
            // handleOption.put("return", true);
        }
        if (orderStatus == 202) {
            handleOption.put("cancel_refund", true);
        }
        if (orderStatus == 300) {
            // TODO: Add logic if needed
        }
        if (orderStatus == 203) {
            handleOption.put("delete", true);
        }
        if (orderStatus == 301) {
            handleOption.put("confirm", true);
        }
        if (orderStatus == 401) {
            handleOption.put("delete", true);
        }
        return handleOption;
    }

    /**
     * 获取订单文本状态码
     *
     * @return Map<String, Boolean>
     */
    public Map<String, Boolean> getOrderTextCode(Integer orderStatus) {
        Map<String, Boolean> textCode = new HashMap<>();
        textCode.put("pay", false);
        textCode.put("close", false);
        textCode.put("delivery", false);
        textCode.put("receive", false);
        textCode.put("success", false);
        textCode.put("countdown", false);
        switch (orderStatus) {
            case 101:
                textCode.put("pay", true);
                textCode.put("countdown", true);
                break;
            case 102:
            case 103:
                textCode.put("close", true);
                break;
            case 201:
            case 300:
                textCode.put("delivery", true);
                break;
            case 301:
                textCode.put("receive", true);
                break;
            case 401:
                textCode.put("success", true);
                break;
        }
        return textCode;
    }

    /**
     * 获取订单状态文本描述
     *
     * @return String
     */
    public String getOrderStatusText(Integer orderStatus) {
        String statusText = "待付款";
        switch (orderStatus) {
            case 101:
                statusText = "待付款";
                break;
            case 102:
            case 103:
                statusText = "交易关闭";
                break;
            case 201:
            case 300:
                statusText = "待发货";
                break;
            case 301:
                statusText = "已发货";
                break;
            case 401:
                statusText = "交易成功";
                break;
        }
        return statusText;
    }

    /**
     * 设置订单支付时间
     *
     * @param orderId 订单ID
     * @param payTime 支付时间
     * @return boolean
     */
    public boolean setOrderPayTime(String orderId, Date payTime) {
        return orderMapper.updatePayTime(orderId, payTime) > 0;
    }

    /**
     * 删除订单（逻辑删除）
     *
     * @param orderId 订单ID
     * @return boolean
     */
    public boolean orderDeleteById(String orderId) {
        return orderMapper.logicDeleteById(orderId) > 0;
    }

    /**
     * 检查订单支付状态
     *
     * @param orderId 订单ID
     * @return boolean
     */
    public boolean checkPayStatus(String orderId) {
        return orderMapper.countPaidOrder(orderId) == 0;
    }

    /**
     * 更新订单支付状态
     *
     * @param orderId   订单ID
     * @param payStatus 支付状态
     * @return boolean
     */
    public boolean updatePayStatus(String orderId, int payStatus) {
        return orderMapper.updatePayStatus(orderId, payStatus) > 0;
    }

    /**
     * 更新订单状态
     *
     * @param orderId     订单ID
     * @param orderStatus 订单状态
     * @return boolean
     */
    public boolean updateOrderStatus(String orderId, int orderStatus) {
        return orderMapper.updateOrderStatus(orderId, orderStatus) > 0;
    }

    /**
     * 根据订单编号查找订单信息
     *
     * @param orderSn 订单编号
     * @return Order
     */
    public Order getOrderByOrderSn(String orderSn) {
        if (orderSn == null || orderSn.isEmpty()) {
            return null;
        }
        return orderMapper.findByOrderSn(orderSn);
    }

    /**
     * 更新订单支付数据
     *
     * @param orderId 订单ID
     * @param info    支付信息
     * @return boolean
     */
    public boolean updatePayData(String orderId, Map<String, Object> info) {
        Map<String, Object> data = new HashMap<>();
        data.put("pay_status", 2);
        data.put("order_status", 300);
        data.put("pay_id", info.get("transaction_id"));
        data.put("pay_time", info.get("time_end"));
        return orderMapper.updatePayData(orderId, data) > 0;
    }


    public List<Integer> getOrderStatus(int showType) {
        switch (showType) {
            case 0:
                return Arrays.asList(101, 102, 103, 201, 202, 203, 300, 301, 302, 303, 401);
            case 1:
                return Arrays.asList(101); // 待付款订单
            case 2:
                return Arrays.asList(300); // 待发货订单
            case 3:
                return Arrays.asList(301); // 待收货订单
            case 4:
                return Arrays.asList(302, 303); // 待评价订单
            default:
                return null;
        }
    }


}
