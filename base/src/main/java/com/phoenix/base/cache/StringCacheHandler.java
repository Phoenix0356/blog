package com.phoenix.base.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class StringCacheHandler extends RedisCacheHandler{


    public StringCacheHandler(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    public void set(String key, Object object){
        redisTemplate.opsForValue().set(key,object,DEFAULT_EXPIRE_TIME,DEFAULT_TIME_UNIT);
    }

    public void set(String key, Object object, Long time){
        redisTemplate.opsForValue().set(key,object,time,DEFAULT_TIME_UNIT);
    }

    public Object getCache(String key, Class<?> clazz){
        Long expireTime = redisTemplate.getExpire(key);
        if (expireTime != null && expireTime > 0){
            redisTemplate.expire(key, expireTime, DEFAULT_TIME_UNIT);
        }
        return new ObjectMapper().convertValue(redisTemplate.opsForValue().get(key),clazz);
    }

    public void deleteCache(String key){
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) return;
        redisTemplate.delete(key);
    }
}
