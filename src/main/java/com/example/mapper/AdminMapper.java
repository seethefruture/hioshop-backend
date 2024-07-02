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

    void updateUsernameAndPassword(@Param("id") String id, @Param("username") String username, @Param("password") String password);

    void updateUsername(@Param("id") String id, @Param("username") String username);

    Admin selectByUsername(@Param("username") String username);

    void deleteById(@Param("id") Long id);
}
