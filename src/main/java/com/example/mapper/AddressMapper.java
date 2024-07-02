package com.example.mapper;

import com.example.po.Address;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {
    @Select("SELECT * FROM address WHERE id = #{id} AND user_id = #{userId}")
    Address findByIdAndUserId(@Param("userId") Long userId, @Param("id") Long id);

    @Select("SELECT * FROM address WHERE id = #{id}")
    Address findById(Long id);

    @Insert("INSERT INTO address (name, mobile, province_id, city_id, district_id, address, user_id, is_default) VALUES (#{name}, #{mobile}, #{provinceId}, #{cityId}, #{districtId}, #{address}, #{userId}, #{isDefault})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Address address);

    @Update("UPDATE address SET name = #{name}, mobile = #{mobile}, province_id = #{provinceId}, city_id = #{cityId}, district_id = #{districtId}, address = #{address}, is_default = #{isDefault} WHERE id = #{id} AND user_id = #{userId}")
    void update(Address address);

    @Update("UPDATE address SET is_delete = 1 where id = #{id}")
    int delete(@Param("id") Long id);

//    @Update("UPDATE address SET is_default = 1 WHERE user_id = #{userId} AND id <> #{id}")
//    void setDefaultAddresses(@Param("userId") Long userId, @Param("id") Long id);

    @Select("SELECT * FROM address WHERE user_id = #{userId} AND is_default = 1 AND is_delete = 0")
    Address getDefaultAddress(@Param("userId") Long userId);

    List<Address> findAddressFullNameByUserId(@Param("userId") Long userId);

    Address findAddressFullNameById(@Param("id") Long addressId);
}
