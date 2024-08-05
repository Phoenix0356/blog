package com.phoenix.blog.context;

import com.phoenix.blog.constant.JwtConstant;
import io.jsonwebtoken.Claims;

import java.util.Date;

public class TokenContext {
    private static final ThreadLocal<Claims> threadLocal = new ThreadLocal<>();

    public static void setClaims(Claims claims){
        threadLocal.set(claims);
    }
    public static String getUserId(){
        return (String) threadLocal.get().get(JwtConstant.SUBTITLE);
    }

    public static String getUserRole(){
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
