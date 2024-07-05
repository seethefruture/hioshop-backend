package com.example.mapper;

import com.example.po.FreightTemplatePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FreightTemplateMapper {

    List<FreightTemplatePO> findFreightTemplates();
}
