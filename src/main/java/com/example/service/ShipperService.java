package com.example.service;

import com.example.mapper.ShipperMapper;
import com.example.po.ShipperPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipperService {

    @Autowired
    private ShipperMapper shipperMapper;

    public List<ShipperPO> getEnabledShippers() {
        return shipperMapper.findEnabledShippers();
    }
}
