package com.example.mapper;

import com.example.po.Shipper; // Assuming Shipper is your POJO class
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShipperMapper {

    String getShipperNameByCode(@Param("shipperCode") String shipperCode);

    Shipper getShipperById(@Param("shipperId") Long shipperId);
}
