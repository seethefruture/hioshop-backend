package com.example.mapper;

import com.example.vo.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsMapper {

    @Select("SELECT * FROM goods WHERE id = #{id}")
    Goods findById(@Param("id") String id);

    @Select("SELECT * FROM goods WHERE id in #{idList}")
    List<Goods> findByIdList(@Param("idList") List<String> idList);


    @Select("SELECT * FROM goods WHERE is_on_sale = #{isOnSale} and is_delete=#{isDelete} and category_id=#{categoryId} ORDER BY  #{sortOrder} limit #{offset},#{limit}")
    List<Goods> selectGoods(@Param("isOnSale") int isOnSale, @Param("isDelete") int isDelete, @Param("categoryId") Long categoryId, @Param("sortOrder") String sortOrder, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM goods WHERE is_on_sale = #{isOnSale} and is_delete=#{isDelete} and category_id=#{categoryId} ")
    int countGoods(@Param("isOnSale") int isOnSale, @Param("isDelete") int isDelete, @Param("categoryId") Long categoryId);

    @Select("SELECT * FROM goods WHERE is_delete = 0")
    List<Goods> selectAll();

    @Select("SELECT * FROM goods WHERE id = #{id} AND is_delete = 0")
    Goods selectById(@Param("id") String id);


    @Select("  SELECT name, retail_price FROM goods WHERE id = #{id}")
    Goods selectShareById(@Param("id") String id);

    List<Goods> selectByParams(Map<String, Object> params);

    @Select("SELECT COUNT(id) FROM goods WHERE is_delete = 0 AND is_on_sale = 1")
    int count();

    @Select("SELECT id, list_pic_url, is_new, goods_number, name, min_retail_price,category_id FROM goods WHERE category_id in #{categoryIdList} AND goods_number >= 0 AND is_on_sale = 1 AND is_index = 1 AND is_delete = 0 ORDER BY sort_order ASC")
    List<Goods> findCategoryGoods(@Param("categoryIdList") List<String> categoryIdList);

    List<Goods> findGoods(@Param("name") String name, @Param("page") int page, @Param("size") int size);

    Goods findGoodsById(@Param("id") String id);

    int insertGoods(Goods goods);

    void updateGoodsNumber(@Param("id") String id, @Param("goodsNumber") int goodsNumber);

    List<Goods> findAllGoods();

    List<Goods> findOnSaleGoods(@Param("page") int page, @Param("size") int size);

    List<Goods> findOutOfStockGoods(@Param("page") int page, @Param("size") int size);

    List<Goods> findDroppedGoods(@Param("page") int page, @Param("size") int size);

    List<Goods> findGoodsSortedBySellVolume(@Param("page") int page, @Param("size") int size);

    List<Goods> findGoodsSortedByRetailPrice(@Param("page") int page, @Param("size") int size);

    List<Goods> findGoodsSortedByNumber(@Param("page") int page, @Param("size") int size);

    void updateSaleStatus(@Param("id") String id, @Param("saleStatus") int saleStatus);

    List<String> findFreightTemplates();

    List<String> findCategories();

    List<Goods> findLevel1Categories();

    List<Goods> findLevel2Categories();

    List<Goods> findLevel2CategoriesByParentId(String parentId);

    void updateIndexShowStatus(String id, Integer status);

    void updateSortOrder(String id, Integer sort);

    void updateShortName(String id, String shortName);

    void updateListPicUrl(String id, String url);

    void markAsDeleted(String id);

    void update(Goods goods);

    String add(Goods goods);

    int countOnSale();
}
