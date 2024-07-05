package com.example.api;

import com.example.po.OrderExpressPO;
import com.example.service.OrderExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderExpressController {

    @Autowired
    private OrderExpressService orderExpressService;

    @GetMapping("/express/{orderId}")
    public ResponseEntity<OrderExpressPO> getLatestOrderExpress(@PathVariable String orderId) {
        OrderExpressPO orderExpress = orderExpressService.getLatestOrderExpress(orderId);
        return ResponseEntity.ok(orderExpress);
    }
}
