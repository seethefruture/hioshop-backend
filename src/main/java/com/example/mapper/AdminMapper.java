package com.example.mapper;

import com.example.po.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {

    List<Admin> selectAll();

    Admin selectById(@Param("id") Long id);

    void insert(Admin admin);

    void update(Admin admin);

    Admin selectByUsername(@Param("username") String username, @Param("id") Long id);

    void deleteById(@Param("id") Long id);

    Map<String, Object> selectShowSettings();

    void updateShowSettings(Map<String, Object> settings);

    void updateAutoStatus(@Param("status") Boolean status);

    void updateShipperSettings(Map<String, Object> settings);

    Map<String, Object> selectSenderInfo();
}
