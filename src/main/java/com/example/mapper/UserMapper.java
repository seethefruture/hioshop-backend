package com.example.mapper;

import com.example.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    String findIdByWeixinOpenid(@Param("weixinOpenid") String weixinOpenid);

    User findById(@Param("id") String id);

    int updateLoginEvent(Long currentTime, String clientIp, String userId);

    Integer insert(User user);

    int update(User user);
}
