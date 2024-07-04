package com.example.mapper;

import com.example.po.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsMapper {

    @Select("SELECT * FROM goods WHERE id = #{id}")
    Goods findById(@Param("id") String id);

    @Select("SELECT * FROM goods WHERE id in #{idList}")
    List<Goods> findByIdList(@Param("idList") List<String> idList);


    @Select("SELECT * FROM goods WHERE is_on_sale = #{isOnSale} and is_delete=#{isDelete} and category_id=#{categoryId} ORDER BY  #{sortOrder} limit #{offset},#{limit}")
    List<Goods> selectGoods(@Param("isOnSale") int isOnSale, @Param("isDelete") int isDelete, @Param("categoryId") Long categoryId, @Param("sortOrder") String sortOrder, @Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM goods WHERE is_on_sale = #{isOnSale} and is_delete=#{isDelete} and category_id=#{categoryId} ")
    int countGoods(@Param("isOnSale") int isOnSale, @Param("isDelete") int isDelete, @Param("categoryId") Long categoryId);

    @Select("SELECT * FROM goods WHERE is_delete = 0")
    List<Goods> selectAll();

    List<Goods> selectByParams(Map<String, Object> params);

    @Select("SELECT COUNT(id) FROM goods WHERE is_delete = 0 AND is_on_sale = 1")
    int countOnSale();

    @Select("SELECT id, list_pic_url, is_new, goods_number, name, min_retail_price,category_id FROM goods WHERE category_id in #{categoryIdList} AND goods_number >= 0 AND is_on_sale = 1 AND is_index = 1 AND is_delete = 0 ORDER BY sort_order ASC")
    List<Goods> findCategoryGoods(@Param("categoryIdList") List<String> categoryIdList);

    @Select("select * from goods limit #{offset},#{limit}")
    List<Goods> findGoods(@Param("offset") int offset, @Param("limit") int limit);

    int insert(Goods goods);

    @Update("update goods set goods_number=#{goodsNumber} where id = #{id}")
    void updateGoodsNumber(@Param("id") String id, @Param("goodsNumber") int goodsNumber);

    @Select("SELECT * FROM goods WHERE is_delete = 0 AND is_on_sale = 1 limit #{offset},#{limit}")
    List<Goods> findOnSaleGoods(@Param("offset") int offset, @Param("limit") int limit);

    @Select("select * from goods where goods_number <=0 and is_delete=0 limit #{offset},#{limit}")
    List<Goods> findOutOfStockGoods(@Param("offset") int offset, @Param("limit") int limit);

    @Select("select * from goods where is_on_sale=0 and is_delete=0 limit #{offset},#{limit}")
    List<Goods> findDroppedGoods(@Param("offset") int offset, @Param("limit") int limit);

    @Select("select * from goods where is_delete=0 order by  #{sort} DESC limit #{offset},#{limit}")
    List<Goods> findGoodsSortDESC(@Param("sort") String sort, @Param("offset") int offset, @Param("limit") int limit);

    @Update("update goods set is_on_sale =#{saleStatus} where id =#{id}")
    void updateSaleStatus(@Param("id") String id, @Param("saleStatus") Boolean saleStatus);

    @Update("update goods set is_index=#{status} where id = #{id}")
    void updateIndexShowStatus(@Param("id") String id, @Param("status") Boolean status);

    @Update("update goods set sort_order =#{sort}  where id=#{id}")
    void updateSortOrder(@Param("id") String id, @Param("sort") Integer sort);

    @Update("update goods set list_pic_url = '' where id=#{id}")
    void updateListPicUrl(String id, String url);

    @Update("update goods set is_delete = 1 where id=#{id}")
    void delete(String id);

    @Update("update goods set category_id = #{categoryId},is_index=#{isIndex},is_new=#{isNew} where id=#{id}")
    void updateStore(@Param("categoryId") String categoryId, @Param("isIndex") Boolean isIndex, @Param("isNew") Boolean isNew, @Param("id") String id);
}
