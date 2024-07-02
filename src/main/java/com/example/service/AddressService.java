package com.example.service;

import com.example.mapper.AddressMapper;
import com.example.mapper.RegionMapper;
import com.example.vo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private RegionMapper regionMapper;

    public List<Address> getAddresses(String userId) {
        return addressMapper.findAddressFullNameByUserId(userId);
    }

    public Address saveAddress(String userId, Address address) {
        address.setUserId(userId);
        if (address.getId() == null) {
            addressMapper.insert(address);
        } else {
            addressMapper.update(address);
        }
//        if (address.getIsDefault() == 1) {
//            addressMapper.setDefaultAddresses(userId, address.getId());
//        }
        return address;
    }

    public boolean deleteAddress(String id) {
        int affectedRows = addressMapper.delete(id);
        return affectedRows > 0;
    }

    public Address getAddressDetail(String userId, String id) {
        return addressMapper.findAddressFullNameById(userId);
    }
}
