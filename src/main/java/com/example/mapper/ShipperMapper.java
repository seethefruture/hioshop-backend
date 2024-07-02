package com.example.mapper;

import com.example.vo.Shipper; // Assuming Shipper is your POJO class
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShipperMapper {

    String getShipperNameByCode(@Param("shipperCode") String shipperCode);

    Shipper getShipperById(@Param("shipperId") Long shipperId);

    @Select("SELECT * FROM shipper WHERE enabled = 1")
    List<Shipper> findEnabledShippers();


    void updateShipperSettings(Map<String, Object> settings);
}
