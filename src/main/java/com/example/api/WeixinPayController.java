package com.example.api;

import com.example.service.WeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class WeixinPayController {

    @Autowired
    private WeixinService weixinService;

    @PostMapping("/preWeixinPaya")
    public ResponseEntity<?> preWeixinPaya(@RequestParam("orderId") String orderId) {
        try {
            weixinService.preWeixinPaya(orderId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/preWeixinPay")
    public ResponseEntity<?> preWeixinPay(@RequestParam("orderId") String orderId) {
        try {
            Map<String, Object> result = weixinService.preWeixinPay(orderId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/notify")
    public ResponseEntity<?> notify(@RequestBody Map<String, Object> payload) {
        try {
            String result = weixinService.notify(payload);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
