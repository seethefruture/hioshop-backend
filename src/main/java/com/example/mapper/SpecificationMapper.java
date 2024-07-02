package com.example.mapper;

import com.example.po.Specification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SpecificationMapper {

    Specification findById(@Param("id") Long id);
}
