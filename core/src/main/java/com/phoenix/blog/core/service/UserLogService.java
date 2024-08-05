package com.phoenix.blog.core.service;


import com.phoenix.blog.model.entity.User;

public interface UserLogService {
    void getUserLogByUserId(String userId);

    void saveUserLog(User user);
}
