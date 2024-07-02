package com.example.mapper;

import com.example.po.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper {

    @Select("SELECT * FROM notice WHERE is_delete = 0")
    List<Notice> findActiveNotices();

    @Update("UPDATE notice SET is_delete = #{isDelete} WHERE id = #{id}")
    int update(Notice notice);
}
