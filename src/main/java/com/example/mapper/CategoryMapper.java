package com.example.mapper;

import com.example.po.Category;
import org.apache.ibatis.annotations.*;

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

    @Select("SELECT * FROM category ORDER BY sort_order ASC")
    List<Category> selectAllCategories();

    @Update("UPDATE category SET sort_order = #{sort} WHERE id = #{id}")
    int updateSortOrder(@Param("id") Long id, @Param("sort") Integer sort);

    @Select("SELECT * FROM category WHERE parent_id = 0 ORDER BY id ASC")
    List<Category> selectTopCategory();

    @Update("UPDATE category SET is_show = #{status} WHERE id = #{id}")
    void updateShowStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE category SET is_channel = #{status} WHERE id = #{id}")
    void updateChannelStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE category SET is_category = #{status} WHERE id = #{id}")
    void updateCategoryStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE category SET img_url = '' WHERE id = #{id}")
    void deleteBannerImage(@Param("id") Long id);

    @Update("UPDATE category SET icon_url = '' WHERE id = #{id}")
    void deleteIconImage(@Param("id") Long id);

    @Insert("INSERT INTO category (/*columns*/) VALUES (/*values*/)") // 请根据实际情况填写列和值
    void insertCategory(Category category);

    @Update("UPDATE category SET /*columns*/ WHERE id = #{id}") // 请根据实际情况填写列和值
    void updateCategory(Category category);

    @Select("SELECT * FROM category WHERE parent_id = #{parentId}")
    List<Category> selectSubCategories(@Param("parentId") Long parentId);

    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteCategory(@Param("id") Long id);
}

