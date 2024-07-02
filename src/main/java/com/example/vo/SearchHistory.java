package com.example.vo;

import lombok.Data;

@Data
public class SearchHistory {
    private String id;
    private String userId;
    private String keyword;
    private Long addTime;

    public SearchHistory(String userId, String keyword, Long addTime) {
        this.userId = userId;
        this.keyword = keyword;
        this.addTime = addTime;
    }
}
