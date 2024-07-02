package com.example.mapper;

import com.example.vo.Specification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SpecificationMapper {

    Specification findById(@Param("id") String id);
}
