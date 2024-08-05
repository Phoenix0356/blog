package com.phoenix.blog.core.service.impl;

import com.phoenix.blog.core.mapper.UserLogMapper;
import com.phoenix.blog.core.service.UserLogService;
import com.phoenix.blog.model.entity.User;
import com.phoenix.blog.model.entity.UserLog;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@Service
@RequiredArgsConstructor
public class UserLogServiceImpl implements UserLogService {

    final UserLogMapper userLogMapper;
    @Override
    public void getUserLogByUserId(String userId) {
    }

    @Override
    @Async("asyncServiceExecutor")
    public void saveUserLog(User user) {
        UserLog userLog = new UserLog();
        userLog.setUserId(user.getUserId())
                .setUsername(user.getUsername())
                .setUserLoginTime(new Timestamp(System.currentTimeMillis()));
        userLogMapper.insert(userLog);
    }
}
