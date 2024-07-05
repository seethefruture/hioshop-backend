package com.example.po;

import lombok.Data;

@Data
public class OrderExpressPO {
    private String id; // 物流信息表的唯一标识
    private String orderId; // 订单ID
    private String shipperId; // 物流公司ID
    private String shipperName; // 物流公司名称
    private String shipperCode; // 物流公司代码
    private String logisticCode; // 快递单号
    private String traces; // 物流跟踪信息
    private boolean isFinish; // 是否完成
    private int requestCount; // 总查询次数
    private int requestTime; // 最近一次向第三方查询物流信息时间
    private Long addTime; // 添加时间
    private Long updateTime; // 更新时间
    private boolean expressType; // 快递类型
    private String regionCode; // 快递的地区编码，如杭州571
}
