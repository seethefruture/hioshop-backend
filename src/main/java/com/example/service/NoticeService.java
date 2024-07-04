package com.example.service;

import com.alibaba.fastjson.JSONObject;
import com.example.mapper.NoticeMapper;
import com.example.vo.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    public List<Map<String, Object>> getAllNotices() throws Exception {
        List<Map<String, Object>> notices = noticeMapper.selectAll();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        for (Map<String, Object> notice : notices) {
            Long endTime = (Long) notice.get("end_time");
            if (endTime != null) {
                notice.put("end_time", sdf.format(new Date(endTime * 1000)));
            }
        }

        return notices;
    }

    public Map<String, Object> updateContent(Long id, String content) throws Exception {
        noticeMapper.updateContent(id, content);
        return new JSONObject().fluentPut("id", id).fluentPut("content", content);
    }

    public Map<String, Object> add(String content, String time) throws Exception {
        long endTime = parseDateToEpoch(time);
        Notice notice = new Notice();
        notice.setContent(content);
        notice.setEndTime(endTime);
        noticeMapper.insert(notice);
        return new JSONObject().fluentPut("id", notice.getId()).fluentPut("content", content).fluentPut("end_time", endTime);
    }

    public Map<String, Object> update(String id, String content, String time) throws Exception {
        long endTime = parseDateToEpoch(time);
        long currentTime = System.currentTimeMillis() / 1000;

        Notice notice = new Notice();
        notice.setId(id);
        notice.setContent(content);
        notice.setEndTime(endTime);
        notice.setIsDelete(endTime > currentTime);
        noticeMapper.update(notice);
        return new JSONObject().fluentPut("id", id ).fluentPut("content", content).fluentPut("end_time", endTime);
    }

    public void destroy(Long id) throws Exception {
        noticeMapper.delete(id);
    }

    private long parseDateToEpoch(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parsedDate = sdf.parse(date);
        return parsedDate.getTime() / 1000;
    }
}
