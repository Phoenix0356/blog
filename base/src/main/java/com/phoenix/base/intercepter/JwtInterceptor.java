package com.phoenix.base.intercepter;

import com.phoenix.base.config.JwtConfig;
import com.phoenix.common.constant.CommonConstant;
import com.phoenix.base.context.TokenContext;
import com.phoenix.common.enumeration.Authorization;
import com.phoenix.common.exceptions.clientException.JwtValidatingException;
import com.phoenix.common.util.DataUtil;
import com.phoenix.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    final JwtConfig jwtConfig;
    final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!DataUtil.isEmptyData(token) && token.startsWith(Authorization.PREFIX.getValue())) {
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
