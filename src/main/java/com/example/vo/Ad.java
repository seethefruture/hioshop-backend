package com.example.vo;

import lombok.Data;

@Data
public class Ad {
    private String id;
    private Boolean enabled;
    private Long endTime;
    private String linkType;
    private Long goodsId;
    private String imageUrl;
    private String link;
    private String name;
    private String endTimeFormatted;
    private int sortOrder;
    private Boolean isDelete;

    // Getters and Setters
}
