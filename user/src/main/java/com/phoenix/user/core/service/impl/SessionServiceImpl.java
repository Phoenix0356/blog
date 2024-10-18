package com.phoenix.user.core.service.impl;

import com.phoenix.common.enumeration.Role;
import com.phoenix.common.util.JwtUtil;
import com.phoenix.user.config.JwtConfig;
import com.phoenix.user.core.manager.SessionManager;
import com.phoenix.user.core.manager.UserManager;
import com.phoenix.user.core.service.SessionService;
import com.phoenix.user.model.entity.User;
import com.phoenix.user.model.pojo.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionManager sessionManager;
    private final UserManager userManager;
    private final JwtConfig jwtConfig;

    @Override
    public void upgradeSession(String userId,String sessionId) {
        Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            return;
        }
        //重新设置session内容
        User user = userManager.selectByUserIdInCache(userId);
        session.setUserId(userId);
        session.setRole(user.getUserRole());
        sessionManager.setSession(sessionId, session);
    }

    @Override
    public String setSession() {
        Session session = new Session();
        String sessionId = UUID.randomUUID().toString();

        session.setUserId(null);
        session.setRole(Role.VISITOR);

        sessionManager.setSession(sessionId,session);
        return sessionId;
    }

    @Override
    public void deleteSession(String sessionKey) {
        sessionManager.deleteSession(sessionKey);
    }

    @Override
    public String generateToken(String sessionId) {
        Session session = sessionManager.getSession(sessionId);
        Role role = session.getRole()!=null?session.getRole():Role.VISITOR;
        return JwtUtil.generateJwt(session.getUserId(), role.name(),
                jwtConfig.secret, jwtConfig.expiration);
    }
}
