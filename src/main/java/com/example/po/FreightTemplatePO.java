package com.example.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FreightTemplatePO {
    private String id;  // ID，使用VARCHAR(64)
    private String name;  // 运费模板名称
    private BigDecimal packagePrice;  // 包装费用
    private Byte freightType;  // 0按件，1按重量
    private Byte isDelete;  // 是否删除

}
