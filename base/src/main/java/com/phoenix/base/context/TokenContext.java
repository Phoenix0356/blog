package com.phoenix.base.context;

import com.phoenix.common.constant.JwtConstant;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.Map;

public class TokenContext {
    private static final ThreadLocal<Claims> threadLocal = new ThreadLocal<>();

    public static void setClaims(Claims claims){
        threadLocal.set(claims);
    }
    public static String getUserId(){
        Map<String, Object> claims = threadLocal.get();
        if (claims == null) {return null;}
        return (String) claims.get(JwtConstant.SUBTITLE);
    }

    public static String getUserRole(){
        Map<String, Object> claims = threadLocal.get();
        if (claims == null) {return null;}
        return (String) threadLocal.get().get(JwtConstant.ROLE);
    }

    public static Date getExpirationTime(){
        return threadLocal.get().getExpiration();
    }

    public static String getJti(){
        return threadLocal.get().getId();
    }

    public static void removeClaims(){
        threadLocal.remove();
    }
}
