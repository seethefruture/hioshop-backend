package com.example.mapper;

import com.example.po.KeywordsPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface KeywordsMapper {

    @Select("SELECT * FROM keywords WHERE is_default = 1 LIMIT 1")
    KeywordsPO findDefaultKeyword();

    @Select("SELECT DISTINCT keyword, is_hot FROM keywords LIMIT 10")
    List<KeywordsPO> findHotKeywords();

    @Select("SELECT DISTINCT keyword FROM keywords WHERE keyword LIKE #{pattern} LIMIT 10")
    List<String> findKeywordsByPattern(String pattern);
}
