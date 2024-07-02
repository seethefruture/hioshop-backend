package com.example.po;

import lombok.Data;

@Data
public class Region {

    private Long id;
    private String name;
    private Long parentId;
}
