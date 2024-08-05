package com.phoenix.blog.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisCacheHandler {
//    private final String LOCK_KEY = "Lock";
//    private final String LOCK_VALUE = "val";
    private final long DEFAULT_EXPIRE_TIME = 86400;
    private final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;


    final RedisTemplate<String,Object> redisTemplate;
    final StringRedisTemplate stringRedisTemplate;

    public void setCache(String key, Object object){
        redisTemplate.opsForValue().set(key,object,DEFAULT_EXPIRE_TIME,DEFAULT_TIME_UNIT);
    }

    public void setCache(String key, Object object, Long time){
        redisTemplate.opsForValue().set(key,object,time,DEFAULT_TIME_UNIT);
    }

    public void setCache(String key, String value){
        stringRedisTemplate.opsForValue().set(key,value,DEFAULT_EXPIRE_TIME,DEFAULT_TIME_UNIT);
    }
    public void setCache(String key, String value, Long time){
        stringRedisTemplate.opsForValue().set(key,value,time,DEFAULT_TIME_UNIT);
    }
    public Object getCache(String key, Class<?> clazz){
        Long expireTime = redisTemplate.getExpire(key);
        if (expireTime != null && expireTime > 0){
            redisTemplate.expire(key, expireTime, DEFAULT_TIME_UNIT);
        }
        return new ObjectMapper().convertValue(redisTemplate.opsForValue().get(key),clazz);
    }

    public Object getCache(String key){
        Long expireTime = redisTemplate.getExpire(key);
        if (expireTime != null && expireTime > 0){
            redisTemplate.expire(key, expireTime, DEFAULT_TIME_UNIT);
        }
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void deleteCache(String key){
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) return;
        redisTemplate.delete(key);
    }
    public void deleteStringCache(String key){
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) return;
        stringRedisTemplate.delete(key);
    }
    //尝试获取锁
//    public void tryLock(){
//        Boolean result = false;
//
//        while (Boolean.FALSE.equals(result)) {
//            result = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, LOCK_VALUE);
//            if (Boolean.FALSE.equals(result)) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }
}
