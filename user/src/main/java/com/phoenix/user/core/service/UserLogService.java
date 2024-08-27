package com.phoenix.user.core.service;


import com.phoenix.user.model.entity.User;

public interface UserLogService {
    void getUserLogByUserId(String userId);

    void saveUserLog(User user, String userOperation);
}
