package com.example.api;

import com.example.vo.Order;
import com.example.vo.Region;
import com.example.vo.UpdateOrderDto;
import com.example.service.OrderService;
import com.example.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public Map<String, Object> listAction(@RequestParam int showType, @RequestParam int page, @RequestParam int size) {
        String userId = getLoginUserId();
        return orderService.listAllOrders(userId, showType, page, size);
    }

    @GetMapping("/count")
    public Map<String, Object> countAction(@RequestParam int showType) {
        String userId = getLoginUserId();
        return orderService.countAction(userId, showType);
    }

    @GetMapping("/orderCount")
    public Map<String, Object> orderCountAction() {
        String userId = getLoginUserId();
        return orderService.orderCountAction(userId);
    }

    @GetMapping("/detail")
    public Map<String, Object> detail(@RequestParam("orderId") String orderId) {
        String userId = getLoginUserId();
        return orderService.getOrderDetail(orderId, userId);
    }

    @GetMapping("/goods")
    public Map<String, Object> orderGoods(@RequestParam("orderId") String orderId) {
        String userId = getLoginUserId();
        return orderService.getOrderGoods(orderId, userId);
    }

    @PostMapping("/cancel")
    public Map<String, Object> cancel(@RequestParam("orderId") String orderId) {
        String userId = getLoginUserId();
        return orderService.cancelOrder(orderId, userId);
    }

    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestParam("orderId") String orderId) {
        return orderService.deleteOrder(orderId);
    }

    @PostMapping("/confirm")
    public Map<String, Object> confirmOrder(@RequestParam("orderId") String orderId) throws Exception {
        return orderService.confirmOrder(orderId);
    }

    @GetMapping("/complete")
    public Map<String, Object> completeOrder(@RequestParam("orderId") String orderId) throws Exception {
        return orderService.completeOrder(orderId);
    }

    @PostMapping("/submit")
    public Map<String, Object> submitOrder(@RequestBody Map<String, Object> orderDto) throws Exception {
        return orderService.submitOrder(orderDto);
    }

    @PostMapping("/update")
    public Map<String, Object> updateOrder(@RequestBody UpdateOrderDto updateOrderDto) {
        return orderService.updateOrder(updateOrderDto.getAddressId(), updateOrderDto.getOrderId());
    }

    @GetMapping("/express")
    public Map<String, Object> getExpressInfo(@RequestParam("orderId") String orderId) throws Exception {
        return orderService.getExpressInfo(orderId);
    }

    @GetMapping("/index")
    public Map<String, Object> index(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "orderSn", defaultValue = "") String orderSn,
            @RequestParam(value = "consignee", defaultValue = "") String consignee,
            @RequestParam(value = "logistic_code", defaultValue = "") String logisticCode,
            @RequestParam(value = "status", defaultValue = "") String status) {

        try {
            Map<String, Object> data = orderService.getOrders(page, size, orderSn, consignee, logisticCode, status);
            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error(500, e.getMessage());
        }
    }

    @GetMapping("/auto-status")
    public Map<String, Object> getAutoStatus() {
        try {
            boolean info = orderService.getAutoStatus();
            return ResponseUtil.success(info);
        } catch (Exception e) {
            return ResponseUtil.error(500, e.getMessage());
        }
    }

    @GetMapping("/to-delivery")
    public Map<String, Object> toDelivery(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", defaultValue = "") String status) {

        try {
            Map<String, Object> data = orderService.getDeliveryOrders(page, size, status);
            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error(500, e.getMessage());
        }
    }

    @PostMapping("/save-goods-list")
    public Map<String, Object> saveGoodsList(@RequestBody Map<String, Object> payload) {
        try {
            String orderSn = orderService.saveGoodsList(payload);
            return ResponseUtil.success(orderSn);
        } catch (Exception e) {
            return ResponseUtil.error(500, e.getMessage());
        }
    }

    @PostMapping("/goods-list-delete")
    public Map<String, Object> goodsListDelete(@RequestBody Map<String, Object> payload) {
        try {
            String orderSn = orderService.goodsListDelete(payload);
            return ResponseUtil.success(orderSn);
        } catch (Exception e) {
            return ResponseUtil.error(500, e.getMessage());
        }
    }

    @PostMapping("/save-admin-memo")
    public Map<String, Object> saveAdminMemo(@RequestBody Map<String, Object> payload) {
        try {
            boolean result = orderService.saveAdminMemo(payload);
            return ResponseUtil.success(result);
        } catch (Exception e) {
            return ResponseUtil.error(500, e.getMessage());
        }
    }

    @PostMapping("/savePrintInfo")
    public Map<String, Object> savePrintInfo(@RequestParam Long id, @RequestParam String printInfo) {
        return orderService.savePrintInfo(id, printInfo);
    }

    @PostMapping("/saveExpressValueInfo")
    public Map<String, Object> saveExpressValueInfo(@RequestParam Long id, @RequestParam String expressValue) {
        return orderService.saveExpressValueInfo(id, expressValue);
    }

    @PostMapping("/saveRemarkInfo")
    public Map<String, Object> saveRemarkInfo(@RequestParam Long id, @RequestParam String remark) {
        return orderService.saveRemarkInfo(id, remark);
    }

    @GetMapping("/detail")
    public Map<String, Object> getOrderDetail(@RequestParam Long orderId) {
        return orderService.getOrderDetail(orderId);
    }

    @GetMapping("/getAllRegion")
    public List<Region> getAllRegion() {
        return orderService.getAllRegion();
    }

    @PostMapping("/orderPack")
    public void orderPack(@RequestParam Long orderId) {
        orderService.orderPack(orderId);
    }

    @PostMapping("/orderReceive")
    public void orderReceive(@RequestParam Long orderId) {
        orderService.orderReceive(orderId);
    }

    @PostMapping("/orderPrice")
    public void orderPrice(@RequestParam Long orderId,
                           @RequestParam Double goodsPrice,
                           @RequestParam Double freightPrice,
                           @RequestParam Double actualPrice) {
        orderService.orderPrice(orderId, goodsPrice, freightPrice, actualPrice);
    }

    @PostMapping("/getOrderExpress")
    public Map<String, Object> getOrderExpress(@RequestParam Long orderId) {
        return orderService.getOrderExpress(orderId);
    }

    @PostMapping("/getPrintTest")
    public Map<String, Object> getPrintTest() {
        return orderService.getPrintTest();
    }

    @PostMapping("/getMianExpress")
    public Map<String, Object> getMianExpress(@RequestParam Long orderId,
                                              @RequestBody Map<String, Object> sender,
                                              @RequestBody Map<String, Object> receiver) {
        return orderService.getMianExpress(orderId, sender, receiver);
    }

    @PostMapping("/rePrintExpress")
    public Map<String, Object> rePrintExpress(@RequestParam Long orderId) {
        return orderService.rePrintExpress(orderId);
    }

    @PostMapping("/directPrintExpress")
    public Map<String, Object> directPrintExpress(@RequestParam Long orderId) {
        return orderService.directPrintExpress(orderId);
    }

    @PostMapping("/goDelivery")
    public Map<String, Object> goDelivery(@RequestParam Long orderId) {
        return orderService.goDelivery(orderId);
    }

    @PostMapping("/goPrintOnly")
    public Map<String, Object> goPrintOnly(@RequestParam Long orderId) {
        return orderService.goPrintOnly(orderId);
    }

    @PostMapping("/orderDelivery")
    public Map<String, Object> orderDelivery(@RequestParam Long orderId,
                                             @RequestParam Integer method,
                                             @RequestParam(required = false) Long deliveryId,
                                             @RequestParam(required = false) String logisticCode) {
        return orderService.orderDelivery(orderId, method, deliveryId, logisticCode);
    }

    @PostMapping("/checkExpress")
    public Map<String, Object> checkExpress(@RequestParam Long orderId) {
        return orderService.checkExpress(orderId);
    }

    @PostMapping("/saveAddress")
    public Map<String, Object> saveAddress(@RequestParam String orderSn,
                                           @RequestParam String name,
                                           @RequestParam String mobile,
                                           @RequestParam String cAddress,
                                           @RequestParam List<Long> addOptions) {
        return orderService.saveAddress(orderSn, name, mobile, cAddress, addOptions);
    }

    @PostMapping("/store")
    public Map<String, Object> store(@RequestBody Order order) {
        return orderService.store(order);
    }

    @PostMapping("/changeStatus")
    public Map<String, Object> changeStatus(@RequestParam String orderSn, @RequestParam Integer status) {
        return orderService.changeStatus(orderSn, status);
    }

    @PostMapping("/destroy")
    public Map<String, Object> destroy(@RequestParam Long id) {
        return orderService.destroy(id);
    }

    @PostMapping("/getGoodsSpecification")
    public List<Map<String, Object>> getGoodsSpecification(@RequestParam Long goodsId) {
        return orderService.getGoodsSpecification(goodsId);
    }

    private String getLoginUserId() {
        // 实现获取当前登录用户ID的逻辑
        return ""; // 临时返回一个示例值
    }

}
