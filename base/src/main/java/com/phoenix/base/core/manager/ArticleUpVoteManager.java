package com.phoenix.base.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.base.cache.SetCacheHandler;
import com.phoenix.base.core.mapper.ArticleUpvoteMapper;
import com.phoenix.common.enumeration.CachePrefix;
import com.phoenix.base.model.entity.ArticleUpvoteRelation;
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
    final SetCacheHandler setCacheHandler;

    private String assembleCacheKey(String id){
        return CachePrefix.BASE_UPVOTE +":"+id;
    }

    private String removeKeyPrefix(String key){
        String[] keyParts = key.split(":");
        return keyParts[keyParts.length-1];
    }
    private void loadCacheIfNotExist(String key){
        //如果文章点赞集合在缓存中,直接返回
        if (setCacheHandler.existKey(key)) {
            return;
        }
        List<Object> upvoteUserList = selectListByArticleId(removeKeyPrefix(key))
                .stream()
                .map(articleUpvoteRelation -> (Object) articleUpvoteRelation.getUpvoteUserId())
                .toList();
        if (!upvoteUserList.isEmpty()) {
            setCacheHandler.setFromList(key,upvoteUserList);
        }
    }

    public Set<String> loadAndGetCache(String articleId){
        String key = assembleCacheKey(articleId);
        loadCacheIfNotExist(key);
        return setCacheHandler.getSet(key)
                .stream()
                .map(userId -> (String)userId)
                .collect(Collectors.toSet());
    }

    public int getArticleUpvoteCount(String articleId){
        String key = assembleCacheKey(articleId);
        loadCacheIfNotExist(key);
        return Math.toIntExact(setCacheHandler.size(key));
    }
    public Boolean isArticleUpvoteByUser(String articleId,String userId){
        String key = assembleCacheKey(articleId);
        loadCacheIfNotExist(key);
        return setCacheHandler.hasMember(key,userId);
    }

    public void addUpvoteUser(String articleId, String userId){
        String key = assembleCacheKey(articleId);
        loadCacheIfNotExist(key);
        setCacheHandler.set(key,userId);
    }

    public void deleteUpvoteUser(String articleId, String userId){
        String key = assembleCacheKey(articleId);
        loadCacheIfNotExist(key);
        setCacheHandler.deleteFromSet(key,userId);
        //从数据库中删除
        delete(articleId,userId);
    }

    public List<ArticleUpvoteRelation> selectListByArticleId(String articleId){
        return articleUpvoteMapper
                .selectList(new QueryWrapper<ArticleUpvoteRelation>().eq("article_id",articleId));
    }

    public void importCachePersistence(){
        Set<String> keys = setCacheHandler.scanKeys(CachePrefix.BASE_UPVOTE.name());
        keys.forEach(key->{
            Set<Object> userIdSet = setCacheHandler.getSet(key);
            userIdSet.forEach(userId->{
                //插入记录行
                ArticleUpvoteRelation articleUpvoteRelation = new ArticleUpvoteRelation();
                articleUpvoteRelation.setArticleId(removeKeyPrefix(key))
                                .setUpvoteUserId((String) userId);
                articleUpvoteMapper.insertIgnore(articleUpvoteRelation);
            });
            //删除缓存key
            setCacheHandler.deleteKey(key);
        });
        log.info("{} 个点赞缓存数据入库成功",keys.size());
    }

    public void delete(String articleId,String userId){
        int result = articleUpvoteMapper.delete(new QueryWrapper<ArticleUpvoteRelation>()
                .eq("article_id",articleId)
                .eq("upvote_user_id",userId)
        );
        if (result == 0){
            log.error("ID为 {} 的用户对ID为 {} 的文章的点赞记录从数据库删除失败",userId,articleId);
        }
    }

}
