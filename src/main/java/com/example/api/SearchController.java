package com.example.api;

import com.example.service.SearchService;
import com.example.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/index")
    public Map<String, Object> index() {
        Long userId = getLoginUserId();
        return ResponseUtil.success(searchService.getIndexData(userId));
    }

    @GetMapping("/helper")
    public Map<String, Object> helper(@RequestParam String keyword) {
        List<String> keywords = searchService.getHelperKeywords(keyword);
        return ResponseUtil.success(keywords);
    }

    @PostMapping("/clearhistory")
    public Map<String, Object> clearHistory() {
        Long userId = getLoginUserId();
        searchService.clearSearchHistory(userId);
        return ResponseUtil.success();
    }

    private Long getLoginUserId() {
        // TODO: Implement getLoginUserId logic
        return 1L; // Placeholder
    }
}
