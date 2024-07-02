package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface ShowSettingsMapper {

    @Select("SELECT * FROM show_settings WHERE id = #{id}")
    Map<String, Object> findById(@Param("id") Long id);
}
