package com.example.po;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class OrderPO {

    private String id; // 订单ID
    private String orderSn; // 订单编号
    private String userId; // 用户ID
    private int orderStatus; // 订单状态
    private Integer offlinePay; // 线下支付订单标志
    private int shippingStatus; // 发货状态
    private boolean printStatus; // 打印状态
    private int payStatus; // 支付状态
    private String consignee; // 收货人
    private String country; // 国家
    private String province; // 省份
    private String city; // 城市
    private String district; // 区域
    private String address; // 收货地址
    private String printInfo; // 打印信息
    private String mobile; // 联系电话
    private String postscript; // 备注信息
    private String adminMemo; // 管理员备注
    private BigDecimal shippingFee; // 运费
    private String payName; // 支付名称
    private String payId; // 支付ID
    private BigDecimal changePrice; // 改价金额
    private BigDecimal actualPrice; // 实际支付金额
    private BigDecimal orderPrice; // 订单总价
    private BigDecimal goodsPrice; // 商品总价
    private Long addTime; // 添加时间
    private Long payTime; // 付款时间
    private Long shippingTime; // 发货时间
    private Long confirmTime; // 确认时间
    private Long dealdoneTime; // 成交时间
    private BigDecimal freightPrice; // 配送费用
    private BigDecimal expressValue; // 顺丰保价金额
    private String remark; // 备注
    private int orderType; // 订单类型
    private int isDelete; // 删除标志

    private List<OrderGoodsPO> goodsList; // @Transient
    private Integer goodsCount; // @Transient
    private String orderStatusText; // @Transient
    private Map<String, Boolean> handleOption; // @Transient
    private String provinceName; // @Transient
    private String cityName; // @Transient
    private String districtName; // @Transient
    private String fullRegion; // @Transient
    private Long finalPayTime; // @Transient
    private UserPO userInfo; // @Transient
    private String expressInfo; // @Transient
    private String buttonText; // @Transient
}
