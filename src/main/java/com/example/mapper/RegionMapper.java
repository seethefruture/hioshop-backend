package com.example.mapper;

import com.example.vo.Region;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface RegionMapper {

    @Select("SELECT * FROM region WHERE id = #{regionId}")
    Region getRegionInfo(@Param("regionId") Long regionId);

    @Select("SELECT * FROM region WHERE parent_id = #{parentId}")
    List<Region> getRegionList(@Param("parentId") Long parentId);

    @Select("SELECT id,name FROM region WHERE id in #{regionId}")
    Map<String, String> getManyRegionNameById(@Param("regionId") List<String> regionId);

//    @Select("SELECT id FROM region WHERE name = #{name}")
//    Long getIdByName(@Param("name") Long name);


//    Map<String, Long> getCodeByName(@Param("province") Long province, @Param("city") Long city, @Param("country") Long country);
}
