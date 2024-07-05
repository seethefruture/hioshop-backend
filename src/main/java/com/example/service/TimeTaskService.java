package com.example.service;

import com.example.mapper.AdMapper;
import com.example.mapper.NoticeMapper;
import com.example.mapper.OrderMapper;
import com.example.mapper.SettingsMapper;
import com.example.po.OrderPO;
import com.example.po.AdPO;
import com.example.po.NoticePO;
import com.example.po.SettingsPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TimeTaskService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AdMapper adMapper;

    @Autowired
    private SettingsMapper settingsMapper;

    public void timetask() {
        long currentTime = System.currentTimeMillis() / 1000;
        long newday = new Date().getTime() / 1000;
        long newdayOver = new Date().getTime() / 1000;

        if (currentTime > newday && currentTime < newdayOver) {
            // Do something if needed
        }

        // Update notice
        List<NoticePO> notices = noticeMapper.findActiveNotices();
        for (NoticePO notice : notices) {
            if (currentTime > notice.getEndTime()) {
                notice.setIsDelete(true);
                noticeMapper.update(notice);
            }
        }

        // Update orders
        long expireTime = currentTime - 24 * 60 * 60;
        List<OrderPO> orders = orderMapper.findExpiredOrders(expireTime);
        for (OrderPO order : orders) {
            order.setOrderStatus(102);
            orderMapper.update(order);
        }

        // Update ads
        List<AdPO> ads = adMapper.findExpiredAds();
        for (AdPO ad : ads) {
            adMapper.updateEnable(ad.getId(), false);
        }

        // Confirm orders
        long noConfirmTime = currentTime - 5 * 24 * 60 * 60;
        List<OrderPO> noConfirmOrders = orderMapper.findNoConfirmOrders(noConfirmTime);
        for (OrderPO order : noConfirmOrders) {
            order.setOrderStatus(401);
            order.setConfirmTime(currentTime);
            orderMapper.update(order);
        }
    }

    public void resetSql() {
        long time = System.currentTimeMillis() / 1000 + 300;
        SettingsPO settings = settingsMapper.findById(1);
        if (settings != null && settings.getReset()) {
            settings.setCountdown(time);
            settings.setReset(true);
            settingsMapper.update(settings);
            System.out.println("重置了！");
        } else {
            System.out.println("还没到呢！");
        }
    }
}
