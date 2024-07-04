package com.example.po;

import lombok.Data;

@Data
public class SearchHistory {
    private String id; // ID，原为int，现为varchar(64)
    private String keyword; // 搜索关键词
    private String from; // 搜索来源，如PC、小程序、APP等
    private int addTime; // 搜索时间
    private String userId; // 用户ID，原为varchar(45)，未更改
}
