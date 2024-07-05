package com.example.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchHistoryPO {
    private String id; // ID，原为int，现为varchar(64)
    private String keyword; // 搜索关键词
    private String from; // 搜索来源，如PC、小程序、APP等
    private Long addTime; // 搜索时间
    private String userId; // 用户ID，原为varchar(45)，未更改
}
