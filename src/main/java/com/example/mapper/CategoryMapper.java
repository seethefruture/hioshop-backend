package com.example.mapper;

import com.example.po.Category;
import com.example.po.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> selectRootCategories();

    Category selectCategoryById(@Param("id") String id);

    List<Category> findChannelCategories();

    List<Category> findShowCategories();

    int updateSortOrder(@Param("id") String id, @Param("sort") Integer sort);

    List<Category> selectTopCategory();

    @Update("UPDATE category SET is_show = #{status} WHERE id = #{id}")
    void updateShowStatus(@Param("id") String id, @Param("status") Integer status);

    @Update("UPDATE category SET is_channel = #{status} WHERE id = #{id}")
    void updateChannelStatus(@Param("id") String id, @Param("status") Integer status);

    @Update("UPDATE category SET is_category = #{status} WHERE id = #{id}")
    void updateCategoryStatus(@Param("id") String id, @Param("status") Integer status);

    @Update("UPDATE category SET img_url = '' WHERE id = #{id}")
    void deleteBannerImage(@Param("id") String id);

    @Update("UPDATE category SET icon_url = '' WHERE id = #{id}")
    void deleteIconImage(@Param("id") String id);

    void insertCategory(Category category);

    void updateCategory(Category category);

    @Select("SELECT * FROM category WHERE parent_id = #{parentId}")
    List<Category> selectSubCategories(@Param("parentId") Long parentId);

    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteCategory(@Param("id") String id);

    String findCategoryNameById(String categoryId);

    @Select("select  * from category where level =#{level} and parent_id=#{parentId}")
    List<Category> findCategoriesByLevelAndParentId(@Param("level") String level, @Param("parentId") String parentId);

    @Select("select  * from category where level =#{level} and parent_id in #{parentIdList}")
    List<Category> findCategoriesByLevelAndParentIdList(@Param("level") String level, @Param("parentIdList") List<String> parentIdList);
}

