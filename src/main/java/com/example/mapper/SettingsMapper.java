package com.example.mapper;

import com.example.po.SettingsPO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SettingsMapper {

    @Select("SELECT * FROM settings WHERE id = #{id}")
    SettingsPO findById(@Param("id") int id);

    @Update("UPDATE settings SET countdown = #{countdown}, reset = #{reset} WHERE id = #{id}")
    int update(SettingsPO settings);

    int getCountdown();
}
