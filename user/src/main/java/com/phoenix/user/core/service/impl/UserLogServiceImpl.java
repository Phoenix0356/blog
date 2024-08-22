package com.phoenix.user.core.service.impl;

import com.phoenix.user.core.mapper.UserLogMapper;
import com.phoenix.user.core.service.UserLogService;
import com.phoenix.user.model.entity.User;
import com.phoenix.user.model.entity.UserLog;
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