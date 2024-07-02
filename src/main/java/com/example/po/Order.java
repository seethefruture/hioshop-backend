package com.example.po;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Order {
    private String id;
    private String orderSn;
    private Integer orderStatus;
    private Long addTime;
    private int isDelete;
    private Long shippingTime;
    private Long confirmTime;
    private double actualPrice;
    private double freightPrice;
    private int offlinePay;
    private List<OrderGoods> goodsList;
    private int goodsCount;
    private String orderStatusText;
    private Map<String, Boolean> handleOption;
    private String userId;
    private String provinceName;
    private String cityName;
    private String districtName;
    private String fullRegion;
    private String postscript;
    private Long dealdoneTime;
    private Long payTime;
    private String consignee;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String address;
    private Integer payStatus;
    private Integer orderType;
    private Long finalPayTime;
    // Getters and Setters
}
