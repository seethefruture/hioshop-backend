package com.example.po;

import lombok.Data;

@Data
public class FreightTemplateDetail {
    private String id;
    private String templateId;
    private String groupId;
    private String area;
    private boolean isDelete;
}
