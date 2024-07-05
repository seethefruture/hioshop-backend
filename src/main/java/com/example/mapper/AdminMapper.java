package com.example.mapper;

import com.example.po.AdminPO;
import com.example.po.ShowSettingsPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    List<AdminPO> selectAll();

    AdminPO selectById(@Param("id") String id);

    void insert(AdminPO adminPO);

    void updateUsernameAndPassword(@Param("id") String id, @Param("username") String username, @Param("password") String password);

    void updateUsername(@Param("id") String id, @Param("username") String username);

    AdminPO selectByUsername(@Param("username") String username);

    void deleteById(@Param("id") String id);

    void update(ShowSettingsPO settings);
}
