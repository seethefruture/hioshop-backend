package com.example.po;

import lombok.Data;

@Data
public class Keywords {
    private Long id;
    private String keyword;
    private Boolean isHot;
    private Boolean isDefault;
}
