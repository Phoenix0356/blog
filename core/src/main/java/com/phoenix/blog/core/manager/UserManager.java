package com.phoenix.blog.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.cache.RedisCacheHandler;
import com.phoenix.blog.core.mapper.UserMapper;
import com.phoenix.blog.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserManager {
    final UserMapper userMapper;
    final RedisCacheHandler redisCacheHandler;
    public User select(String userId){
        return userMapper.selectById(userId);
    }

    public List<User> selectListByUserId(List<String> userIdList){
        return userMapper.selectList(new QueryWrapper<User>().in("user_id",userIdList));
    }

    public User selectUserInCache(String userId){
        User user = (User) redisCacheHandler.getCache(userId,User.class);
        if (user == null){
            user = userMapper.selectById(userId);
            redisCacheHandler.setCache(userId,user);
        }
        return user;
    }

    public void setIntoCache(String id, String value, Long time){
        redisCacheHandler.setCache(id,value,time);
    }

    public void deleteCache(String id){
        redisCacheHandler.deleteCache(id);
    }
}
