package com.example.mapper;

import com.example.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    String findIdByWeixinOpenid(@Param("weixinOpenid") String weixinOpenid);

    UserPO findById(@Param("id") String id);

    int updateLoginEvent(Long currentTime, String clientIp, String userId);

    Integer insert(UserPO user);

    int update(UserPO user);

    @Select("SELECT COUNT(*) FROM user")
    int countUsers();

    @Select("SELECT * FROM user WHERE register_time > #{timestamp}")
    List<UserPO> findNewUsers(@Param("beginTimeStamp") long beginTimeStamp, @Param("endTimeStamp") long endTimeStamp);

    @Select("SELECT COUNT(*) FROM user WHERE register_time < #{timestamp} AND last_login_time > #{timestamp}")
    int countOldUsers(@Param("beginTimeStamp") long beginTimeStamp, @Param("endTimeStamp") long endTimeStamp);
}
