package com.example.mapper;

import com.example.po.RegionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface RegionMapper {

    @Select("SELECT * FROM region WHERE id = #{regionId}")
    RegionPO getRegionInfo(@Param("regionId") Long regionId);

    @Select("SELECT * FROM region WHERE parent_id = #{parentId}")
    List<RegionPO> getRegionList(@Param("parentId") Long parentId);

    @Select("SELECT id,name FROM region WHERE id in #{regionId}")
    Map<String, String> getManyRegionNameById(@Param("regionId") List<String> regionId);

    @Select("select * from region")
    List<RegionPO> findAll();


//    @Select("SELECT id FROM region WHERE name = #{name}")
//    Long getIdByName(@Param("name") Long name);


//    Map<String, Long> getCodeByName(@Param("province") Long province, @Param("city") Long city, @Param("country") Long country);
}
