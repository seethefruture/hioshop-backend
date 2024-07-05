package com.example.mapper;

import com.example.po.SearchHistoryPO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SearchHistoryMapper {

    @Insert("INSERT INTO search_history (user_id, keyword, add_time)\n" +
            "        VALUES (#{userId}, #{keyword}, #{addTime})")
    int insert(SearchHistoryPO searchHistory);

    @Select("SELECT DISTINCT keyword FROM search_history WHERE user_id = #{userId} LIMIT 10")
    List<String> findHistoryKeywordsByUserId(Long userId);

    @Delete("DELETE FROM search_history WHERE user_id = #{userId}")
    void deleteByUserId(Long userId);
}
