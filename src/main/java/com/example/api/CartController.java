package com.example.api;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.po.Cart;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/index")
    public ResponseEntity<?> index(@RequestParam int type) {
        Map<String, Object> result = cartService.getCarts(type);
        return ResponseEntity.ok(result);
    }

    /**
     * 复购
     *
     * @param goodsId
     * @param productId
     * @param number
     * @return
     */
    @PostMapping("/addAgain")
    public ResponseEntity<?> addAgain(@RequestParam String goodsId, @RequestParam String productId, @RequestParam int number) {
        cartService.addAgain(goodsId, productId, number);
        return ResponseEntity.ok("Added successfully");
    }

    /**
     * 从购物车删除商品
     *
     * @param productIds
     * @return
     */
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody List<String> productIds) {
        String userId = getLoginUserId(); // Implement this method to get the logged-in user ID.
        if (CollUtil.isEmpty(productIds)) {
            return ResponseEntity.badRequest().body("删除出错");
        }
        cartService.deleteProducts(userId, productIds);
        return ResponseEntity.ok(cartService.getCarts(userId, false, null));
    }

    @GetMapping("/goodsCount")
    public ResponseEntity<?> getGoodsCount() {
        String userId = getLoginUserId();
        List<Cart> cartData = cartService.getCarts(userId, false, null);
        cartService.updateFastCart(userId);
        JSONObject result = new JSONObject().fluentPut("cartTotal", new JSONObject().fluentPut("goodsCount", cartData.size()));
        return ResponseEntity.ok(result);
    }

    /**
     * 看起来像是下单
     *
     * @param orderFrom
     * @param type
     * @param addressId
     * @param addType
     * @return
     */
    @GetMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestParam("orderFrom") Long orderFrom,
                                      @RequestParam("type") Integer type,
                                      @RequestParam("addressId") String addressId,
                                      @RequestParam("addType") Integer addType) {
        String userId = getLoginUserId(); // Implement this method to get the logged-in user ID.
        return ResponseEntity.ok(cartService.checkout(userId, orderFrom, type, addressId, addType));
    }

    @GetMapping("/goodsList")
    public List<Cart> getGoodsList(@RequestParam String userId) {
        return cartService.getGoodsList(userId);
    }

    @GetMapping("/checkedGoodsList")
    public List<Cart> getCheckedGoodsList(@RequestParam String userId) {
        return cartService.getCheckedGoodsList(userId);
    }

    /**
     * 清空购物车
     *
     * @param userId
     * @return
     */
    @PostMapping("/clearBuyGoods")
    public int clearBuyGoods(@RequestParam String userId) {
        return cartService.clearBuyGoods(userId);
    }

    private String getLoginUserId() {
        // Mock implementation to return a user ID.
        // Replace with actual logic to get the logged-in user's ID.
        return "";
    }
}
