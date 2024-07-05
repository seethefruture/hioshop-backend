package com.example.po;

import lombok.Data;

@Data
public class ExceptAreaPO {

    private String id; // 唯一标识符

    private String content; // 名称

    private String area; // 区域，默认为 0

    private boolean isDelete; // 是否删除，0 表示未删除，1 表示已删除

}
