package com.phoenix.gateway.filter.cookie;

import jakarta.servlet.http.Cookie;

import java.util.HashMap;

public class CookieUtil {
    public static Cookie generateDefaultCookie(String name,String value) {
        Cookie cookie = new Cookie(name,value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 8);
        return cookie;
    }

    public static String cookieToString(Cookie cookie) {
        StringBuilder cookieStrBuilder = new StringBuilder();

        // 添加名称和值
        cookieStrBuilder.append(cookie.getName()).append('=').append(cookie.getValue());
        // 添加Max-Age
        if (cookie.getMaxAge() >= 0) {
            cookieStrBuilder.append("; Max-Age=").append(cookie.getMaxAge());
        }

        // 添加Path
        if (cookie.getPath()!=null && !cookie.getPath().isEmpty()) {
            cookieStrBuilder.append("; Path=").append(cookie.getPath());
        }

        // 添加Domain（如果有）
        if (cookie.getDomain() != null && !cookie.getDomain().isEmpty()) {
            cookieStrBuilder.append("; Domain=").append(cookie.getDomain());
        }

        // 添加Secure标志
        if (cookie.getSecure()) {
            cookieStrBuilder.append("; Secure");
        }

        // 添加HttpOnly标志
        if (cookie.isHttpOnly()) {
            cookieStrBuilder.append("; HttpOnly");
        }

        return cookieStrBuilder.toString();
    }

    public static String getCookieValue(String cookieString) {
        String value = cookieString.split("=")[1];
        return value.split(";")[0];
    }
}
