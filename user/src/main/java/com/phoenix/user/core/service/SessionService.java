package com.phoenix.user.core.service;

import jakarta.annotation.Nullable;

public interface SessionService {

    String setSession();

    void upgradeSession(String userId,String sessionId);

    void deleteSession(String sessionKey);

    String generateToken(String sessionId);
}
