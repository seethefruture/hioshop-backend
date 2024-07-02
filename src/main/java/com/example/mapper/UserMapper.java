package com.example.mapper;

import com.example.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    String findIdByWeixinOpenid(@Param("weixinOpenid") String weixinOpenid);

    User findById(@Param("id") String id);

    int updateLoginEvent(Long currentTime, String clientIp, String userId);

    Integer insert(User user);

    int update(User user);

    @Select("SELECT COUNT(*) FROM user WHERE is_on_sale = 1 AND is_delete = 0")
    int countOnSale();

    @Select("SELECT COUNT(*) FROM user")
    int countUsers();

    @Select("SELECT * FROM user WHERE register_time > #{timestamp}")
    List<User> findNewUsers(int index, long todayTimestamp, long yesTimestamp, long sevenTimestamp, long thirtyTimestamp);

    @Select("SELECT COUNT(*) FROM user WHERE register_time < #{timestamp} AND last_login_time > #{timestamp}")
    int countOldUsers(int index, long todayTimestamp, long yesTimestamp, long sevenTimestamp, long thirtyTimestamp);
}
