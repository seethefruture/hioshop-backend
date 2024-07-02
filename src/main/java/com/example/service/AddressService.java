package com.example.service;

import com.example.mapper.AddressMapper;
import com.example.mapper.RegionMapper;
import com.example.po.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private RegionMapper regionMapper;

    public List<Address> getAddresses(Long userId) {
        return addressMapper.findAddressFullNameByUserId(userId);
    }

    public Address saveAddress(Long userId, Address address) {
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

    public boolean deleteAddress(Long id) {
        int affectedRows = addressMapper.delete(id);
        return affectedRows > 0;
    }

    public Address getAddressDetail(Long userId, Long id) {
        return addressMapper.findAddressFullNameById(userId);
    }
}
