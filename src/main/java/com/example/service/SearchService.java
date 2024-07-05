package com.example.service;

import com.example.mapper.KeywordsMapper;
import com.example.mapper.SearchHistoryMapper;
import com.example.po.KeywordsPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private KeywordsMapper keywordsMapper;

    @Autowired
    private SearchHistoryMapper searchHistoryMapper;

    public Map<String, Object> getIndexData(Long userId) {
        KeywordsPO defaultKeyword = keywordsMapper.findDefaultKeyword();
        List<KeywordsPO> hotKeywordList = keywordsMapper.findHotKeywords();
        List<String> historyKeywordList = searchHistoryMapper.findHistoryKeywordsByUserId(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("defaultKeyword", defaultKeyword);
        data.put("historyKeywordList", historyKeywordList);
        data.put("hotKeywordList", hotKeywordList);
        return data;
    }

    public List<String> getHelperKeywords(String keyword) {
        return keywordsMapper.findKeywordsByPattern(keyword + "%");
    }

    public void clearSearchHistory(Long userId) {
        searchHistoryMapper.deleteByUserId(userId);
    }
}
