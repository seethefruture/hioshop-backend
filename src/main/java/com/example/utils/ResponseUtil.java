package com.example.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    
    public static Map<String, Object> success() {
        return success(null);
    }

    public static Map<String, Object> success(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("errno", 0);
        response.put("errmsg", "成功");
        response.put("data", data);
        return response;
    }

    public static Map<String, Object> error(int errno, String errmsg) {
        Map<String, Object> response = new HashMap<>();
        response.put("errno", errno);
        response.put("errmsg", errmsg);
        return response;
    }
}
