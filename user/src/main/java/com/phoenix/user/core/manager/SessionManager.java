package com.phoenix.user.core.manager;

import com.phoenix.common.enumeration.CachePrefix;
import com.phoenix.user.cache.StringCacheHandler;
import com.phoenix.user.model.pojo.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionManager {
    final StringCacheHandler stringCacheHandler;

    private String assembleCacheKey(String id){
        return CachePrefix.USER_SESSION +":"+id;
    }
    public void setSession(String id,Session session) {
        String key = assembleCacheKey(id);
        stringCacheHandler.set(key, session);
    }
    public Session getSession(String sessionId) {
        String key = assembleCacheKey(sessionId);
        return (Session) stringCacheHandler.getCache(key,Session.class);
    }

    public void deleteSession(String sessionId) {
        String key = assembleCacheKey(sessionId);
        stringCacheHandler.deleteCache(key);
    }
}
