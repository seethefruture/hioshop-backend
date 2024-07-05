package com.example.mapper;

import com.example.po.ShipperPO; // Assuming com.example.po.ShipperPO is your POJO class
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShipperMapper {

    String getShipperNameByCode(@Param("shipperCode") String shipperCode);

    ShipperPO getShipperById(@Param("shipperId") Long shipperId);

    @Select("SELECT * FROM shipper WHERE enabled = 1")
    List<ShipperPO> findEnabledShippers();


    void updateShipperSettings(Map<String, Object> settings);
}
