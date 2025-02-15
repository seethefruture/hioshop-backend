package com.example.service;

import com.example.mapper.RegionMapper;
import com.example.po.RegionPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RegionService {

    @Autowired
    private RegionMapper regionMapper;

    public RegionPO getRegionInfo(Long regionId) {
        return regionMapper.getRegionInfo(regionId);
    }

    public List<RegionPO> getRegionList(Long parentId) {
        return regionMapper.getRegionList(parentId);
    }

    public List<RegionPO> getData(Long parentId) {
        return regionMapper.getRegionList(parentId);
    }

    public Map<String, Long> getCode(Long province, Long city, Long country) {
        throw new RuntimeException("这个功能可能有bug");
//        return regionMapper.getCodeByName(province, city, country);
//        data.put("province_id", regionMapper.getIdByName(province));
//        data.put("city_id", regionMapper.getIdByName(city));
//        data.put("country_id", regionMapper.getIdByName(country));
    }
}
