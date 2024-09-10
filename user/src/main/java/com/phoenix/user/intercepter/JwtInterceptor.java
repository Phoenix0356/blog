package com.phoenix.user.intercepter;

import com.phoenix.user.config.JwtConfig;
import com.phoenix.user.context.TokenContext;
import com.phoenix.common.util.DataUtil;
import com.phoenix.common.util.JwtUtil;
import com.phoenix.common.constant.CommonConstant;
import com.phoenix.common.exceptions.clientException.JwtValidatingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    final JwtConfig jwtConfig;
    final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (!DataUtil.isEmptyData(token) && token.startsWith("Bearer ")){
            token = token.substring(7);
            try {
                Claims claims = JwtUtil.isValidateToken(token,jwtConfig.secret);
                TokenContext.setClaims(claims);

                if (JwtUtil.isBlackListedToken(stringRedisTemplate,claims.getId())){
                    throw new JwtValidatingException("你的令牌已失效");
                }
            }catch (JwtException je){
                throw new JwtValidatingException(CommonConstant.RE_LOGIN);
            }
        }else {
            throw new JwtValidatingException("请登录");
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
