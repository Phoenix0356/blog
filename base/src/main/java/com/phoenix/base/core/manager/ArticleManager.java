package com.phoenix.base.core.manager;

import com.phoenix.base.cache.RedisCacheHandler;
import com.phoenix.base.core.mapper.ArticleMapper;
import com.phoenix.base.model.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleManager {
    private final ArticleMapper articleMapper;
    final RedisCacheHandler redisCacheHandler;

    //从redis缓存中拿数据，如果没有就去数据库中取，再更新到缓存中
    public Article selectArticleInCache(String articleId){
        Article article = (Article) redisCacheHandler.getCache(articleId,Article.class);
        if (article == null){
            article = articleMapper.selectById(articleId);
            redisCacheHandler.setCache(articleId, article);
        }
        return article;
    }

    public void deleteArticleInCache(String articleId){
        if (redisCacheHandler.getCache(articleId) == null) return;
        redisCacheHandler.deleteCache(articleId);
    }

}
