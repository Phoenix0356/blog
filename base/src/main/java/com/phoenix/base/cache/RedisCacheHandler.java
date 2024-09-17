package com.phoenix.base.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;
@Component
@RequiredArgsConstructor
public abstract class RedisCacheHandler {

    final RedisTemplate<String,Object> redisTemplate;
    protected final long DEFAULT_EXPIRE_TIME = 86400;
    protected final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    public Boolean exist(String key){
         return redisTemplate.hasKey(key);
    }

    public Set<String> scanKeys(String prefix){
        return redisTemplate.keys(prefix.concat("*"));
    }
}
