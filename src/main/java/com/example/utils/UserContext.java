package com.example.utils;

import javax.servlet.http.HttpServletRequest;

public class UserContext {

    public static Long getLoginUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    public static Long getTime() {
        return System.currentTimeMillis() / 1000;
    }
}
