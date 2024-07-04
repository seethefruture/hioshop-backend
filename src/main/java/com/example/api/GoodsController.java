package com.example.api;

import com.alibaba.fastjson.JSONObject;
import com.example.vo.Goods;
import com.example.vo.Product;
import com.example.service.GoodsService;
import com.example.service.ProductService;
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

    @Autowired
    private ProductService productService;
    @GetMapping("/index")
    public ResponseEntity<?> index() {
        List<Goods> goodsList = goodsService.getAllGoods();
        return ResponseEntity.ok(goodsList);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> detail(@RequestParam("id") String goodsId, @RequestParam("userId") String userId) {
        Map<String, Object> detail = goodsService.getGoodsDetail(goodsId, userId);
        if (detail == null) {
            return ResponseEntity.badRequest().body("该商品不存在或已下架");
        }
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/share")
    public ResponseEntity<?> goodsShare(@RequestParam("id") String goodsId) {
        Goods info = goodsService.getGoodsShare(goodsId);
        return ResponseEntity.ok(info);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProductList(@RequestParam String goodsId) {
        List<Product> products = goodsService.getProductList(goodsId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/specifications")
    public ResponseEntity<Map<String, Object>> getSpecificationList(@RequestParam String goodsId) {
        Map<String, Object> specifications = goodsService.getSpecificationList(goodsId);
        return ResponseEntity.ok(specifications);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(value = "userId", required = false) String userId,
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

    @GetMapping("/express-data")
    public JSONObject getExpressData() {
        return goodsService.getExpressData();
    }

    @PostMapping("/copy")
    public String copyGoods(@RequestParam("id") String goodsId) {
        return goodsService.copyGoods(goodsId);
    }

    @PostMapping("/update-stock")
    public void updateStock(
            @RequestParam("goods_sn") String goodsSn,
            @RequestParam("goods_number") int goodsNumber) {
        goodsService.updateStock(goodsSn, goodsNumber);
    }

    @PostMapping("/update-goods-number")
    public void updateGoodsNumber() {
        goodsService.updateGoodsNumber();
    }

    @GetMapping("/on-sale")
    public List<Goods> getOnSaleGoods(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size") int size) {
        return goodsService.getOnSaleGoods(page, size);
    }

    @GetMapping("/out-of-stock")
    public List<Goods> getOutOfStockGoods(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size") int size) {
        return goodsService.getOutOfStockGoods(page, size);
    }

    @GetMapping("/dropped")
    public List<Goods> getDroppedGoods(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size") int size) {
        return goodsService.getDroppedGoods(page, size);
    }

    @GetMapping("/sort")
    public List<Goods> sortGoods(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "index") int index) {
        return goodsService.sortGoods(page, size, index);
    }

    @PostMapping("/sale-status")
    public void updateSaleStatus(
            @RequestParam("id") String id,
            @RequestParam("status") boolean status) {
        goodsService.updateSaleStatus(id, status);
    }

    @PostMapping("/status")
    public void updateProductStatus(@RequestParam("id") String id, @RequestParam("status") Integer status) {
        productService.updateProductStatus(id, status);
    }

    @PostMapping("/indexShowStatus")
    public void updateIndexShowStatus(@RequestParam("id") String id, @RequestParam("status") Boolean status) {
        productService.updateIndexShowStatus(id, status);
    }

    @GetMapping("/info")
    public Map<String, Object> getProductInfo(@RequestParam("id") String id) {
        return productService.getProductInfo(id);
    }

    @GetMapping("/categories")
    public Map<String, Object> getAllCategories() {
        return productService.getAllCategories();
    }

    @PostMapping("/store")
    public String storeProduct(@RequestBody Map<String, Object> values) {
        return goodsService.store(values);
    }

    @PostMapping("/updatePrice")
    public void updatePrice(@RequestBody Map<String, Object> data) {
        productService.updatePrice(data);
    }

    @PostMapping("/checkSku")
    public Boolean checkSku(@RequestBody Map<String, Object> info) {
        return productService.checkSku(info);
    }

    @PostMapping("/updateSort")
    public void updateSort(@RequestParam("id") String id, @RequestParam("sort") Integer sort) {
        productService.updateSort(id, sort);
    }

    @GetMapping("/galleryList")
    public Map<String, Object> getGalleryList(@RequestParam("id") String id) {
        return productService.getGalleryList(id);
    }

    @PostMapping("/gallery")
    public void addGallery(@RequestParam("url") String url, @RequestParam("goods_id") String goodsId) {
        productService.addGallery(url, goodsId);
    }

    @PostMapping("/getGalleryList")
    public Map<String, Object> getGalleryListByGoodsId(@RequestParam("goodsId") String goodsId) {
        return productService.getGalleryListByGoodsId(goodsId);
    }

    @PostMapping("/deleteGalleryFile")
    public void deleteGalleryFile( @RequestParam("id") String id) {
        productService.deleteGalleryFile( id);
    }

    @PostMapping("/galleryEdit")
    public void editGallery(@RequestBody Map<String, Object> values) {
        productService.editGallery(values);
    }

    @PostMapping("/deleteListPicUrl")
    public void deleteListPicUrl(@RequestParam("id") String id) {
        productService.deleteListPicUrl(id);
    }

    @PostMapping("/destroy")
    public void destroyProduct(@RequestParam("id") String id) {
        productService.destroyProduct(id);
    }

    @PostMapping("/uploadHttpsImage")
    public String uploadHttpsImage(@RequestParam("url") String url) {
        return productService.uploadHttpsImage(url);
    }
}
