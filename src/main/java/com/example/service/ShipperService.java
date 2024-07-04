package com.example.service;

import com.example.mapper.ShipperMapper;
import com.example.vo.Shipper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipperService {

    @Autowired
    private ShipperMapper shipperMapper;

    public List<Shipper> getEnabledShippers() {
        return shipperMapper.findEnabledShippers();
    }
}
