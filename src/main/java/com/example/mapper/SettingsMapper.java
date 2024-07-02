package com.example.mapper;

import com.example.vo.Settings;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SettingsMapper {

    @Select("SELECT * FROM settings WHERE id = #{id}")
    Settings findById(@Param("id") int id);

    @Update("UPDATE settings SET countdown = #{countdown}, reset = #{reset} WHERE id = #{id}")
    int update(Settings settings);

    int getCountdown();
}
