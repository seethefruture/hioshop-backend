package com.example.mapper;

import com.example.po.ShowSettingsPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface ShowSettingsMapper {

    @Select("SELECT * FROM show_settings WHERE id = #{id}")
    ShowSettingsPO findById(@Param("id") Long id);

    @Select("select * from show_settings")
    ShowSettingsPO selectShowSettings();

    void updateShowSettings(Map<String, Object> settings);

    void updateAutoStatus(@Param("status") Boolean status);

}
