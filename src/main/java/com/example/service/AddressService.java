package com.example.service;

import com.example.mapper.AddressMapper;
import com.example.mapper.RegionMapper;
import com.example.po.AddressPO;
import com.example.utils.MySnowFlakeGenerator;
import com.example.po.AddressPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    public List<AddressPO> getAddresses(String userId) {
        return addressMapper.findAddressFullNameByUserId(userId);
    }

    public AddressPO saveAddress(String userId, AddressPO address) {
        address.setUserId(userId);
        if (address.getId() == null) {
            address.setId(MySnowFlakeGenerator.next());
            addressMapper.insert(address);
        } else {
            addressMapper.update(address);
        }
        return address;
    }

    public boolean deleteAddress(String id) {
        int affectedRows = addressMapper.delete(id);
        return affectedRows > 0;
    }

    public AddressPO getAddressDetail(String userId, String id) {
        return addressMapper.findAddressFullNameById(userId);
    }
}
