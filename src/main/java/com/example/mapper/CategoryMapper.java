package com.example.mapper;

import com.example.po.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("SELECT * FROM category limit #{limit}")
    List<Category> selectCategories(@Param("limit") int limit);

    @Select("SELECT * FROM category where id = #{id}")
    Category selectCategoryById(@Param("id") Long id);

    @Select("SELECT id FROM category WHERE parent_id = #{parentId}")
    List<Long> findIdsByParentId(@Param("parentId") Long parentId);

    @Select("SELECT id, icon_url, name, sort_order FROM category WHERE is_channel = 1 AND parent_id = 0 ORDER BY sort_order")
    List<Category> findChannelCategories();

    @Select("SELECT id, name, img_url AS banner, p_height AS height FROM category WHERE parent_id = 0 AND is_show = 1 ORDER BY sort_order")
    List<Category> findShowCategories();
}

