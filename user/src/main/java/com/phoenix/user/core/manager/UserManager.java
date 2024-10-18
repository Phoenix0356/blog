package com.phoenix.user.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.common.enumeration.CachePrefix;
import com.phoenix.user.cache.RedisCacheHandler;
import com.phoenix.user.cache.StringCacheHandler;
import com.phoenix.user.core.mapper.UserMapper;
import com.phoenix.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserManager {
    final UserMapper userMapper;
    final StringCacheHandler stringCacheHandler;

    private String assembleCacheKey(String id){
        return CachePrefix.USER_CONTENT +":"+id;
    }

    public User select(String userId){
        return userMapper.selectById(userId);
    }

    public List<User> selectListByUserId(List<String> userIdList){
        return userMapper.selectList(new QueryWrapper<User>().in("user_id",userIdList));
    }

    public User selectByUserIdInCache(String userId){
        String key = assembleCacheKey(userId);
        User user = (User) stringCacheHandler.getCache(key,User.class);
        if (user == null){
            user = userMapper.selectById(userId);
            user.setPassword(null);
            stringCacheHandler.set(key,user);
        }
        return user;
    }

    public User selectByUsername(String username){
       return userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
    }

    public void setIntoCache(String id, String value){
        String key = assembleCacheKey(id);
        stringCacheHandler.set(key,value);
    }

    public void deleteCache(String id){
        String key = assembleCacheKey(id);
        stringCacheHandler.deleteCache(key);
    }
}
