package com.example.mapper;

import com.example.po.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsMapper {

    @Select("SELECT * FROM goods WHERE id = #{id}")
    Goods findById(@Param("id") Long id);

    @Select("SELECT * FROM goods WHERE id in #{idList}")
    List<Goods> findByIdList(@Param("idList") List<Long> idList);


    @Select("SELECT * FROM goods WHERE is_on_sale = #{isOnSale} and is_delete=#{isDelete} and category_id=#{categoryId} ORDER BY  #{sortOrder} limit #{offset},#{limit}")
    List<Goods> selectGoods(@Param("isOnSale") int isOnSale, @Param("isDelete") int isDelete, @Param("categoryId") Long categoryId, @Param("sortOrder") String sortOrder, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM goods WHERE is_on_sale = #{isOnSale} and is_delete=#{isDelete} and category_id=#{categoryId} ")
    int countGoods(@Param("isOnSale") int isOnSale, @Param("isDelete") int isDelete, @Param("categoryId") Long categoryId);

    @Select("SELECT * FROM goods WHERE is_delete = 0")
    List<Goods> selectAll();

    @Select("SELECT * FROM goods WHERE id = #{id} AND is_delete = 0")
    Goods selectById(@Param("id") Long id);


    @Select("  SELECT name, retail_price FROM goods WHERE id = #{id}")
    Goods selectShareById(@Param("id") Long id);

    List<Goods> selectByParams(Map<String, Object> params);

    @Select("SELECT COUNT(id) FROM goods WHERE is_delete = 0 AND is_on_sale = 1")
    int count();

    @Select("SELECT id, list_pic_url, is_new, goods_number, name, min_retail_price,category_id FROM goods WHERE category_id in #{categoryIdList} AND goods_number >= 0 AND is_on_sale = 1 AND is_index = 1 AND is_delete = 0 ORDER BY sort_order ASC")
    List<Goods> findCategoryGoods(@Param("categoryIdList") List<Long> categoryIdList);
}
