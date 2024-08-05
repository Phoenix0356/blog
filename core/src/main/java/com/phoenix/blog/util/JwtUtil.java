package com.phoenix.blog.util;

import com.phoenix.blog.constant.JwtConstant;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.UUID;


public class JwtUtil {

    public static String getJwt(String userId,String role,String secret,long expiration){
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(Claims.ISSUER,"phoenix");
        claims.put(JwtConstant.SUBTITLE,userId);
        claims.put(JwtConstant.ROLE,role);
        claims.put(Claims.ID, UUID.randomUUID().toString());
        claims.put(Claims.EXPIRATION,System.currentTimeMillis()/1000+expiration);
        return Jwts.builder()
                .claims(claims)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }


    public static Claims isValidateToken(String token,String secret) throws JwtException {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static boolean isBlackListedToken(StringRedisTemplate stringRedisTemplate,String jti){
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(jti));
    }
}
