package com.example.api;

import com.example.service.WeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class WeixinController {

    @Autowired
    private WeixinService weixinService;

    @PostMapping("/getBase64")
    public ResponseEntity<?> getBase64(@RequestBody Map<String, String> payload) {
        String goodsId = payload.get("goodsId");
        if (goodsId == null || goodsId.isEmpty()) {
            return ResponseEntity.badRequest().body("Goods ID is required");
        }

        try {
            String base64Image = weixinService.getBase64(goodsId);
            return ResponseEntity.ok(base64Image);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/receive")
    public String receiveAction(@RequestBody Map<String, Object> payload) {
        weixinService.receiveAction(payload);
        return "haha";
    }

//    @PostMapping("/notify")
//    public String notifyAction(@RequestBody Map<String, String> params) {
//        if (weixinService.checkSignature(params)) {
//            return params.get("echostr");
//        } else {
//            return "error";
//        }
//    }
}
