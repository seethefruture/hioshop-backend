package com.example.api;

import com.example.po.UpdateOrderDto;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public Map<String, Object> listAction(@RequestParam int showType, @RequestParam int page, @RequestParam int size) {
        Long userId = getLoginUserId();
        return orderService.listAllOrders(userId, showType, page, size);
    }

    @GetMapping("/count")
    public Map<String, Object> countAction(@RequestParam int showType) {
        Long userId = getLoginUserId();
        return orderService.countAction(userId, showType);
    }

    @GetMapping("/orderCount")
    public Map<String, Object> orderCountAction() {
        Long userId = getLoginUserId();
        return orderService.orderCountAction(userId);
    }

    @GetMapping("/detail")
    public Map<String, Object> detail(@RequestParam("orderId") Long orderId) {
        Long userId = getLoginUserId();
        return orderService.getOrderDetail(orderId, userId);
    }

    @GetMapping("/goods")
    public Map<String, Object> orderGoods(@RequestParam("orderId") Long orderId) {
        Long userId = getLoginUserId();
        return orderService.getOrderGoods(orderId, userId);
    }

    @PostMapping("/cancel")
    public Map<String, Object> cancel(@RequestParam("orderId") Long orderId) {
        Long userId = getLoginUserId();
        return orderService.cancelOrder(orderId, userId);
    }

    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestParam("orderId") Long orderId) {
        return orderService.deleteOrder(orderId);
    }

    @PostMapping("/confirm")
    public Map<String, Object> confirmOrder(@RequestParam("orderId") Long orderId) throws Exception {
        return orderService.confirmOrder(orderId);
    }

    @GetMapping("/complete")
    public Map<String, Object> completeOrder(@RequestParam("orderId") Long orderId) throws Exception {
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
    public Map<String, Object> getExpressInfo(@RequestParam("orderId") Long orderId) throws Exception {
        return orderService.getExpressInfo(orderId);
    }

    private Long getLoginUserId() {
        // 实现获取当前登录用户ID的逻辑
        return 1L; // 临时返回一个示例值
    }

}
