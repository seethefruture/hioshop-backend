package com.example.po;

import lombok.Data;

@Data
public class SearchHistory {
    private Long id;
    private Long userId;
    private String keyword;
    private Long addTime;

    public SearchHistory(Long userId, String keyword, Long addTime) {
        this.userId = userId;
        this.keyword = keyword;
        this.addTime = addTime;
    }
}
