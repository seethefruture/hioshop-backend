package com.example.po;

import lombok.Data;

@Data
public class ShowSettingsPO {

    private String id; // `id` 主键, 类型改为 String

    private byte banner; // 滚动banner

    private byte channel; // 是否开启menu,几个图标

    private byte indexBannerImg; // 首页的img图片是否显示

    private byte notice; // 通知设置
}
