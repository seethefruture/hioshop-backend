package com.example.po;

import lombok.Data;

@Data
public class Ad {
    private int id;
    private int enabled;
    private long endTime;
    private String linkType;
    private Long goodsId;
    private String imageUrl;
    private String link;

    // Getters and Setters
}
