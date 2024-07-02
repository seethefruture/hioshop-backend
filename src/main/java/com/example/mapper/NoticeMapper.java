package com.example.mapper;

import com.example.vo.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {

    @Select("SELECT * FROM notice WHERE is_delete = 0")
    List<Notice> findActiveNotices();

    @Select("SELECT * FROM notice")
    List<Map<String, Object>> selectAll();

    @Update("UPDATE notice SET content = #{content} WHERE id = #{id}")
    void updateContent(@Param("id") Long id, @Param("content") String content);

    @Insert("INSERT INTO notice (content, end_time) VALUES (#{content}, #{endTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Notice notice);

    @Update("UPDATE notice SET content = #{content}, end_time = #{endTime}, is_delete = #{isDelete} WHERE id = #{id}")
    void update(Notice notice);

    @Delete("DELETE FROM notice WHERE id = #{id}")
    void delete(@Param("id") Long id);
}
