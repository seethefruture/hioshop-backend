package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.mapper.*;
import com.example.po.OrderGoodsPO;
import com.example.po.OrderPO;
import com.example.po.ProductPO;
import com.example.po.UserPO;
import com.example.utils.MySnowFlakeGenerator;
import com.example.utils.SignatureUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
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
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private SettingsMapper settingsMapper;
    @Autowired
    private CartMapper cartMapper;

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
        UserPO user;
        if (userId == null) {
            // 注册新用户
            user = new UserPO();
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

    public void preWeixinPaya(String orderId) {
        OrderPO orderInfo = orderMapper.findById(orderId);
        if (orderInfo != null) {
            long currentTime = System.currentTimeMillis() / 1000;
            Map<String, Object> result = new HashMap<>();
            result.put("transaction_id", 123123123123L);
            result.put("time_end", currentTime);

            orderMapper.updatePayData(orderInfo.getId(), result);
            afterPay(orderInfo);
        }
    }

    public Map<String, Object> preWeixinPay(String orderId) throws Exception {
        OrderPO orderInfo = orderMapper.findById(orderId);
        if (orderInfo == null || orderInfo.getPayStatus() != 0) {
            throw new Exception("订单已取消或已支付");
        }

        List<OrderGoodsPO> orderGoodsList = orderMapper.findOrderGoodsByOrderId(orderId);
        for (OrderGoodsPO item : orderGoodsList) {
            ProductPO product = productMapper.findById(item.getProductId());
            if (item.getNumber() > product.getGoodsNumber() || !item.getRetailPrice().equals(product.getRetailPrice())) {
                throw new Exception("库存不足或价格发生变化");
            }
        }

        UserPO user = userMapper.findById(orderInfo.getUserId());
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

    private void afterPay(OrderPO orderInfo) {
        if (orderInfo.getOrderType() == 0) {
            List<OrderGoodsPO> orderGoodsList = orderMapper.findOrderGoodsByOrderId(orderInfo.getId());
            for (OrderGoodsPO cartItem : orderGoodsList) {
                productMapper.decrementGoodsNumber(cartItem.getGoodsId(), cartItem.getNumber());
                productMapper.incrementSellVolume(cartItem.getGoodsId(), cartItem.getNumber());
            }
        }
    }

    public void receiveAction(Map<String, Object> payload) {
        UserPO user = new UserPO();
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

    public boolean checkLogin() {
        // Assuming `think.userId` is replaced with some session check logic
        int userId = getCurrentUserId(); // Replace with actual logic to get current user ID
        return userId != 0;
    }

    public Map<String, Object> getIndexInfo() {
        int goodsOnsale = goodsMapper.countOnSale();
        int orderToDelivery = orderMapper.countToDelivery();
        int userCount = userMapper.countUsers();
        int timestamp = settingsMapper.getCountdown();

        Map<String, Object> info = new HashMap<>();
        info.put("user", userCount);
        info.put("goodsOnsale", goodsOnsale);
        info.put("timestamp", timestamp);
        info.put("orderToDelivery", orderToDelivery);

        return info;
    }

    public Map<String, String> getQiniuToken() {
//        String token = qiniuService.getQiniuToken();
        String token = "";
//        String domain = qiniuService.getDomain();
        String domain = "";

        Map<String, String> info = new HashMap<>();
        info.put("token", token);
        info.put("url", domain);

        return info;
    }

    public Map<String, Object> getMainData(int index) {
        Date beginOfToday = DateUtil.beginOfDay(new Date()).toJdkDate();
        long beginOfTodayTimestamp = beginOfToday.getTime();
        long beginOfYesterdayTimestamp = DateUtil.offsetDay(beginOfToday, -1).getTime();
        long beginOfLastSevenDayTimestamp = DateUtil.offsetDay(beginOfToday, -7).getTime();
        long beginOfLastThirtyDayTimestamp = DateUtil.offsetDay(beginOfToday, -30).getTime();
        long beginTimeStamp, endTimeStamp;
        switch (index) {
            case 0: // 今天
                beginTimeStamp = beginOfTodayTimestamp;
                endTimeStamp = System.currentTimeMillis();
                break;
            case 1: // 昨天
                beginTimeStamp = beginOfYesterdayTimestamp;
                endTimeStamp = beginOfTodayTimestamp;
                break;
            case 2: // 七天
                beginTimeStamp = beginOfLastSevenDayTimestamp;
                endTimeStamp = beginOfTodayTimestamp;
                break;
            case 3: // 三十天
                beginTimeStamp = beginOfLastThirtyDayTimestamp;
                endTimeStamp = beginOfTodayTimestamp;
                break;
            default:
                throw new RuntimeException("查询类型暂不支持");
        }

        int addCart = cartMapper.countNewCarts(beginTimeStamp, endTimeStamp);
        List<UserPO> newData = userMapper.findNewUsers(beginTimeStamp, endTimeStamp);
        int newUser = newData.size();
        int oldUser = userMapper.countOldUsers(beginTimeStamp, endTimeStamp);
        int addOrderNum = orderMapper.countNewOrders(beginTimeStamp, endTimeStamp);
        int addOrderSum = orderMapper.sumOrderPrice(beginTimeStamp, endTimeStamp);
        int payOrderNum = orderMapper.countPaidOrders(beginTimeStamp, endTimeStamp);
        int payOrderSum = orderMapper.sumPaidOrderPrice(beginTimeStamp, endTimeStamp);

        Map<String, Object> info = new HashMap<>();
        // Fetch data based on index
        // Add logic for fetching new and old users, order statistics, etc.

        // Sample code to fetch data (you need to implement the actual logic)


        info.put("newUser", newUser);
        info.put("oldUser", oldUser);
        info.put("addCart", addCart);
        info.put("newData", newData);
//        info.put("oldData", oldData);
        info.put("addOrderNum", addOrderNum);
        info.put("addOrderSum", addOrderSum);
        info.put("payOrderNum", payOrderNum);
        info.put("payOrderSum", payOrderSum);

        return info;
    }

    private String formatTimestamp(Long timestamp) {
        return Instant.ofEpochSecond(timestamp).toString(); // Customize as needed
    }

    private int getCurrentUserId() {
        // Implement logic to retrieve the current user ID
        return 0;
    }
}
