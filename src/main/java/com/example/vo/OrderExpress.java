package com.example.vo;

import lombok.Data;

@Data
public class OrderExpress {

    private String orderId;
    private String shipperCode;
    private String logisticCode;
    private int expressStatus;
    private int isFinish;
    private String traces;
    private long updateTime;
    private String shipperName;
    private long requestTime;
    private Integer requestCount;
}