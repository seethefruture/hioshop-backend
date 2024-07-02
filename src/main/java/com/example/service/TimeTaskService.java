package com.example.service;

import com.example.mapper.AdMapper;
import com.example.mapper.NoticeMapper;
import com.example.mapper.OrderMapper;
import com.example.mapper.SettingsMapper;
import com.example.vo.Ad;
import com.example.vo.Notice;
import com.example.vo.Order;
import com.example.vo.Settings;
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
        List<Notice> notices = noticeMapper.findActiveNotices();
        for (Notice notice : notices) {
            if (currentTime > notice.getEndTime()) {
                notice.setIsDelete(true);
                noticeMapper.update(notice);
            }
        }

        // Update orders
        long expireTime = currentTime - 24 * 60 * 60;
        List<Order> orders = orderMapper.findExpiredOrders(expireTime);
        for (Order order : orders) {
            order.setOrderStatus(102);
            orderMapper.update(order);
        }

        // Update ads
        List<Ad> ads = adMapper.findExpiredAds();
        for (Ad ad : ads) {
            ad.setEnabled(false);
            adMapper.updateEnable(ad);
        }

        // Confirm orders
        long noConfirmTime = currentTime - 5 * 24 * 60 * 60;
        List<Order> noConfirmOrders = orderMapper.findNoConfirmOrders(noConfirmTime);
        for (Order order : noConfirmOrders) {
            order.setOrderStatus(401);
            order.setConfirmTime(currentTime);
            orderMapper.update(order);
        }
    }

    public void resetSql() {
        long time = System.currentTimeMillis() / 1000 + 300;
        Settings settings = settingsMapper.findById(1);
        if (settings != null && settings.getReset() == 0) {
            settings.setCountdown(time);
            settings.setReset(1);
            settingsMapper.update(settings);
            System.out.println("重置了！");
        } else {
            System.out.println("还没到呢！");
        }
    }
}
