package com.example.service;

import com.example.mapper.ShowSettingsMapper;
import com.example.po.ShowSettingsPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ShowSettingsService {

    @Autowired
    private ShowSettingsMapper showSettingsMapper;

    public ShowSettingsPO getShowSettings() {
        return showSettingsMapper.findById(1L);
    }
}
