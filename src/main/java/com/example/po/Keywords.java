package com.example.po;

import lombok.Data;

@Data
public class Keywords {
    private String id; // 唯一标识符
    private String keyword; // 关键词
    private int isHot; // 是否热门
    private int isDefault; // 是否默认
    private int isShow; // 是否显示
    private int sortOrder; // 排序字段
    private String schemeUrl; // 关键词的跳转链接
    private int type; // 关键词类型
}
