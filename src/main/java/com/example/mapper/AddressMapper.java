package com.example.mapper;

import com.example.po.AddressPO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {
    AddressPO findById(@Param("id") String id);

    void insert(AddressPO addressPO);

    void update(AddressPO addressPO);

    int delete(@Param("id") String id);

    AddressPO getDefaultAddress(@Param("userId") String userId);

    List<AddressPO> findAddressFullNameByUserId(@Param("userId") String userId);

    AddressPO findAddressFullNameById(@Param("id") String addressId);
}
