package com.example.po;

import lombok.Data;

@Data
public class CategoryPO {
    private String id; // 主键ID
    private String name; // 类别名称
    private String keywords; // 关键词
    private String frontDesc; // 前台描述
    private String parentId; // 父类别ID
    private int sortOrder; // 排序值
    private boolean showIndex; // 是否在首页显示
    private int isShow; // 是否显示
    private String iconUrl; // 图标URL
    private String imgUrl; // 图片URL
    private String level; // 级别
    private String frontName; // 前台名称
    private int pHeight; // 高度
    private boolean isCategory; // 是否为类别
    private boolean isChannel; // 是否为频道
}
