package com.example.service;

import com.alibaba.fastjson.JSONObject;
import com.example.mapper.UserMapper;
import com.example.po.Order;
import com.example.po.OrderGoods;
import com.example.po.User;
import com.example.utils.MySnowFlakeGenerator;
import com.example.utils.SignatureUtil;
import com.example.mapper.OrderMapper;
import com.example.mapper.ProductMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.po.Product;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class WeixinService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserMapper userMapper;

    @Value("${weixin.appid}")
    private String appId;

    @Value("${weixin.secret}")
    private String secret;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;


    public Map<String, Object> loginByWeixin(String code) throws Exception {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        Map<String, Object> sessionData = new ObjectMapper().readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
        });

        if (!sessionData.containsKey("openid")) {
            throw new Exception("登录失败，openid无效");
        }

        String openid = (String) sessionData.get("openid");
        String userId = userMapper.findIdByWeixinOpenid(openid);
        int isNew = 0;
        Long now = System.currentTimeMillis();
        String clientIp = ""; // 暂时不记录 ip
        User user;
        if (userId == null) {
            // 注册新用户
            user = new User();
            user.setUsername("微信用户" + UUID.randomUUID().toString().substring(0, 6));
            user.setPassword(openid);
            user.setRegisterTime(now);
            user.setRegisterIp(clientIp);
            user.setLastLoginTime(now);
            user.setLastLoginIp(clientIp);
            user.setWeixinOpenid(openid);
            user.setNickname(Base64.getEncoder().encodeToString("微信用户".getBytes(StandardCharsets.UTF_8)));
            user.setAvatar("/static/images/default_avatar.png");
            user.setId(MySnowFlakeGenerator.next());
            userMapper.insert(user);
            isNew = 1;
        } else {
            // 更新登录信息
            userMapper.updateLoginEvent(now, clientIp, userId);
            user = userMapper.findById(userId);
        }
        sessionData.put("user_id", userId);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", userId);
        userInfo.put("username", user.getUsername());
        userInfo.put("nickname", new String(Base64.getDecoder().decode(user.getNickname()), StandardCharsets.UTF_8));
        userInfo.put("avatar", user.getAvatar());
        String token = generateToken(sessionData);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", userInfo);
        result.put("is_new", isNew);
        return result;
    }

    private String generateToken(Map<String, Object> sessionData) {
        // Implement token generation logic
        return "token";
    }

    public void preWeixinPaya(Long orderId) {
        Order orderInfo = orderMapper.findById(orderId);
        if (orderInfo != null) {
            long currentTime = System.currentTimeMillis() / 1000;
            Map<String, Object> result = new HashMap<>();
            result.put("transaction_id", 123123123123L);
            result.put("time_end", currentTime);

            orderMapper.updatePayData(orderInfo.getId(), result);
            afterPay(orderInfo);
        }
    }

    public Map<String, Object> preWeixinPay(Long orderId) throws Exception {
        Order orderInfo = orderMapper.findById(orderId);
        if (orderInfo == null || orderInfo.getPayStatus() != 0) {
            throw new Exception("订单已取消或已支付");
        }

        List<OrderGoods> orderGoodsList = orderMapper.findOrderGoodsByOrderId(orderId);
        for (OrderGoods item : orderGoodsList) {
            Product product = productMapper.findById(item.getProductId());
            if (item.getNumber() > product.getGoodsNumber() || !item.getRetailPrice().equals(product.getRetailPrice())) {
                throw new Exception("库存不足或价格发生变化");
            }
        }

        User user = userMapper.findById(orderInfo.getUserId());
        if (user == null || user.getWeixinOpenid() == null) {
            throw new Exception("微信支付失败，没有openid");
        }

        // 调用微信支付接口，省略具体实现
        // Map<String, Object> returnParams = weixinService.createUnifiedOrder(...);
        // return returnParams;
        return new HashMap<>();
    }

    public String notify(Map<String, Object> payload) {
        // 省略具体实现
        return "SUCCESS";
    }

    private void afterPay(Order orderInfo) {
        if (orderInfo.getOrderType() == 0) {
            List<OrderGoods> orderGoodsList = orderMapper.findOrderGoodsByOrderId(orderInfo.getId());
            for (OrderGoods cartItem : orderGoodsList) {
                productMapper.decrementGoodsNumber(cartItem.getGoodsId(), cartItem.getNumber());
                productMapper.incrementSellVolume(cartItem.getGoodsId(), cartItem.getNumber());
            }
        }
    }

    public void receiveAction(Map<String, Object> payload) {
        User user = new User();
        user.setName("9");
        user.setRuleContent("哈哈");
        userMapper.insert(user);
    }

    public boolean checkSignature(Map<String, String> params) {
        return SignatureUtil.checkSignature(params);
    }

    public String getBase64(String goodsId) throws Exception {
        String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + secret;
        ResponseEntity<String> tokenResponse = restTemplate.getForEntity(tokenUrl, String.class);
        JSONObject tokenData = JSONObject.parseObject(tokenResponse.getBody());
        String token = tokenData.getString("access_token");

        String wxacodeUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token;
        JSONObject postData = new JSONObject();
        postData.put("scene", goodsId);
        postData.put("page", "pages/goods/goods");
        postData.put("width", 200);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(postData.toString(), headers);
        ResponseEntity<byte[]> response = restTemplate.postForEntity(wxacodeUrl, request, byte[].class);

        return Base64.getEncoder().encodeToString(response.getBody());
    }
}
