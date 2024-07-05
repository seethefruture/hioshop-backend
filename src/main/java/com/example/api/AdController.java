package com.example.api;

import com.example.po.AdPO;
import com.example.po.GoodsPO;
import com.example.service.GoodsService;
import com.example.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ad")
public class AdController {

    @Autowired
    private AdService adService;
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/index")
    public ResponseEntity<?> index(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> data = adService.getAdList(page, size);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/updateSort")
    public ResponseEntity<?> updateSort(@RequestBody Map<String, Object> payload) {
        String id = (String) payload.get("id");
        int sort = (int) payload.get("sort");
        adService.updateSortOrder(id, sort);
        return ResponseEntity.ok("Sort order updated successfully");
    }

    @GetMapping("/info")
    public ResponseEntity<?> info(@RequestParam String id) {
        AdPO ad = adService.getAdInfo(id);
        return ResponseEntity.ok(ad);
    }

    @PostMapping("/store")
    public ResponseEntity<?> store(@RequestBody AdPO ad) {
        adService.saveAd(ad);
        return ResponseEntity.ok("Ad saved successfully");
    }

    @GetMapping("/getallrelate")
    public ResponseEntity<?> getAllRelate() {
        List<GoodsPO> allGoods = goodsService.getAllGoods();
        return ResponseEntity.ok(allGoods);
    }

    @PostMapping("/destroy")
    public ResponseEntity<?> destroy(@RequestBody Map<String, Object> payload) {
        String id = (String) payload.get("id");
        adService.destroyAd(id);
        return ResponseEntity.ok("Ad destroyed successfully");
    }

    @GetMapping("/saleStatus")
    public ResponseEntity<?> saleStatus(@RequestParam String id, @RequestParam String status) {
        boolean enabled = Boolean.parseBoolean(status);
        adService.updateSaleStatus(id, enabled);
        return ResponseEntity.ok("Sale status updated successfully");
    }
}
