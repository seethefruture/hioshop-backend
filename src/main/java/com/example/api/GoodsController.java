package com.example.api;

import com.example.po.Goods;
import com.example.po.Product;
import com.example.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/index")
    public ResponseEntity<?> index() {
        List<Goods> goodsList = goodsService.getAllGoods();
        return ResponseEntity.ok(goodsList);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> detail(@RequestParam("id") Long goodsId, @RequestParam("userId") Long userId) {
        Map<String, Object> detail = goodsService.getGoodsDetail(goodsId, userId);
        if (detail == null) {
            return ResponseEntity.badRequest().body("该商品不存在或已下架");
        }
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/share")
    public ResponseEntity<?> goodsShare(@RequestParam("id") Long goodsId) {
        Goods info = goodsService.getGoodsShare(goodsId);
        return ResponseEntity.ok(info);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProductList(@RequestParam Long goodsId) {
        List<Product> products = goodsService.getProductList(goodsId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/specifications")
    public ResponseEntity<Map<String, Object>> getSpecificationList(@RequestParam Long goodsId) {
        Map<String, Object> specifications = goodsService.getSpecificationList(goodsId);
        return ResponseEntity.ok(specifications);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(value = "userId", required = false) Long userId,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "sort", required = false) String sort,
                                  @RequestParam(value = "order", required = false) String order,
                                  @RequestParam(value = "sales", required = false) String sales) {
        List<Goods> goodsData = goodsService.getGoodsList(userId, keyword, sort, order, sales);
        return ResponseEntity.ok(goodsData);
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() {
        int goodsCount = goodsService.getGoodsCount();
        return ResponseEntity.ok(goodsCount);
    }
}
