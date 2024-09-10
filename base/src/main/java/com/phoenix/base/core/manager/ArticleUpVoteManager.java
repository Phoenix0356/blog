package com.phoenix.base.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.base.cache.SetCacheHandler;
import com.phoenix.base.cache.StringCacheHandler;
import com.phoenix.base.core.mapper.ArticleUpvoteMapper;
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
public class ArticleUpVoteManager {
    final ArticleUpvoteMapper articleUpvoteMapper;
    final StringCacheHandler stringCacheHandler;
    final SetCacheHandler setCacheHandler;

    private void loadCache(String articleId){
        //如果文章点赞集合不在缓存中,把数据库中数据加载进缓存
        if (!setCacheHandler.exist(articleId)){
            List<Object> upvoteUserList = selectListByArticleId(articleId)
                    .stream()
                    .map(articleUpvote-> (Object)articleUpvote.getUpvoteUserId())
                    .toList();
            setCacheHandler.setList(articleId,upvoteUserList);
        }
    }
    public Set<String> getCache(String articleId){
        loadCache(articleId);
        return setCacheHandler.getSet(articleId)
                .stream()
                .map(userId -> (String)userId)
                .collect(Collectors.toSet());
    }

    public Boolean isArticleUpvoteByUser(String articleId,String userId){
        return setCacheHandler.hasMember(articleId,userId);
    }

    public void setUserIntoSet(String articleId,String userId){
        loadCache(articleId);
        setCacheHandler.set(articleId,userId);
    }

    public void deleteUserFromSet(String articleId,String userId){
        loadCache(articleId);
        setCacheHandler.deleteFromSet(articleId,userId);
    }

    public List<ArticleUpvote> selectListByArticleId(String articleId){
        return articleUpvoteMapper
                .selectList(new QueryWrapper<ArticleUpvote>().eq("article_id",articleId));
    }
}
