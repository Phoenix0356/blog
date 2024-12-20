package com.phoenix.base.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class SetCacheHandler extends RedisCacheHandler{

    public SetCacheHandler(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    public void set(String key, Object object){
        redisTemplate.opsForSet().add(key, object);
        redisTemplate.expire(key, DEFAULT_EXPIRE_TIME, DEFAULT_TIME_UNIT);
    }

    public void setFromList(String key, List<Object> values) {
        redisTemplate.opsForSet().add(key, values.toArray());
    }
    public Set<Object> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }
    public Boolean hasMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public Long size(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public void deleteFromSet(String key, Object value) {
        redisTemplate.opsForSet().remove(key, value);
    }
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}
