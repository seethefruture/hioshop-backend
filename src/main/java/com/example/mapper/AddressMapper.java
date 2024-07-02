package com.example.mapper;

import com.example.po.Address;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {
    Address findById(@Param("id") String id);

    void insert(Address address);

    void update(Address address);

    int delete(@Param("id") String id);

    Address getDefaultAddress(@Param("userId") String userId);

    List<Address> findAddressFullNameByUserId(@Param("userId") String userId);

    Address findAddressFullNameById(@Param("id") String addressId);
}
