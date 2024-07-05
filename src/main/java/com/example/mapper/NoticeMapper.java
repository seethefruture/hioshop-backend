package com.example.mapper;

import com.example.po.NoticePO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {

    @Select("SELECT * FROM notice WHERE is_delete = 0")
    List<NoticePO> findActiveNotices();

    @Select("SELECT * FROM notice")
    List<NoticePO> selectAll();

    @Update("UPDATE notice SET content = #{content} WHERE id = #{id}")
    void updateContent(@Param("id") Long id, @Param("content") String content);

    @Insert("INSERT INTO notice (content, end_time) VALUES (#{content}, #{endTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(NoticePO notice);

    @Update("UPDATE notice SET content = #{content}, end_time = #{endTime}, is_delete = #{isDelete} WHERE id = #{id}")
    void update(NoticePO notice);

    @Delete("update notice set is_delete=1 WHERE id = #{id}")
    void delete(@Param("id") Long id);
}
