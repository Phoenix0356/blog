package com.phoenix.base.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.base.cache.SetCacheHandler;
import com.phoenix.base.cache.StringCacheHandler;
import com.phoenix.base.core.mapper.ArticleUpvoteMapper;
import com.phoenix.base.enumeration.CachePrefix;
import com.phoenix.base.model.entity.ArticleUpvote;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleUpVoteManager{

    final ArticleUpvoteMapper articleUpvoteMapper;
    final StringCacheHandler stringCacheHandler;
    final SetCacheHandler setCacheHandler;

    private String assembleCacheKey(String id){
        return CachePrefix.UPVOTE +":"+id;
    }

    private String removeKeyPrefix(String key){
        String[] keyParts = key.split(":");
        return keyParts[keyParts.length-1];
    }
    private void loadCache(String articleId){
        String key = assembleCacheKey(articleId);
        //如果文章点赞集合在缓存中,直接返回
        if (setCacheHandler.exist(key)) {
            return;
        }
        List<Object> upvoteUserList = selectListByArticleId(key)
                .stream()
                .map(articleUpvote-> (Object)articleUpvote.getUpvoteUserId())
                .toList();
        if (!upvoteUserList.isEmpty()) {
            setCacheHandler.setList(key,upvoteUserList);
        }
    }
    public Set<String> loadAndGetCache(String articleId){
        String key = assembleCacheKey(articleId);
        loadCache(key);
        return setCacheHandler.getSet(key)
                .stream()
                .map(userId -> (String)userId)
                .collect(Collectors.toSet());
    }
    public int getCacheSize(String articleId){
        String key = assembleCacheKey(articleId);
        return Math.toIntExact(setCacheHandler.size(key));
    }
    public Boolean isArticleUpvoteByUser(String articleId,String userId){
        String key = assembleCacheKey(articleId);
        return setCacheHandler.hasMember(key,userId);
    }

    public void setUserIntoCache(String articleId, String userId){
        String key = assembleCacheKey(articleId);
        loadCache(key);
        setCacheHandler.set(key,userId);
    }

    public void deleteUserFromCache(String articleId, String userId){
        String key = assembleCacheKey(articleId);
        loadCache(key);
        setCacheHandler.deleteFromSet(key,userId);
    }

    public List<ArticleUpvote> selectListByArticleId(String articleId){
        return articleUpvoteMapper
                .selectList(new QueryWrapper<ArticleUpvote>().eq("article_id",articleId));
    }

    public void importCachePersistence(){
        Set<String> keys = setCacheHandler.scanKeys(CachePrefix.UPVOTE.name());
        keys.forEach(key->{
            Set<Object> userIdSet = setCacheHandler.getSet(key);
            userIdSet.forEach(userId->{
                ArticleUpvote articleUpvote = new ArticleUpvote();
                articleUpvote.setArticleId(removeKeyPrefix(key));
                articleUpvote.setUpvoteUserId((String) userId);
                articleUpvoteMapper.insert(articleUpvote);
            });
        });
        log.info("{} 个点赞缓存数据入库成功",keys.size());
    }
}
