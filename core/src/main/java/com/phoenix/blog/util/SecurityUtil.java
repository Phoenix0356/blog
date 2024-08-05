package com.phoenix.blog.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityUtil {
    public static BCryptPasswordEncoder passwordEncoder;

    private static class PasswordEncoderHolder{
        public static final BCryptPasswordEncoder INSTANCE = new BCryptPasswordEncoder();
    }

    public static BCryptPasswordEncoder getPasswordEncoder() {
        if (passwordEncoder == null){
            return PasswordEncoderHolder.INSTANCE;
        }
        return passwordEncoder;
    }
}
