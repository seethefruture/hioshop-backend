package com.example.mapper;

import com.example.po.SpecificationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SpecificationMapper {

    SpecificationPO findById(@Param("id") String id);
}
