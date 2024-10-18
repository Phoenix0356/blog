package com.phoenix.base.core.manager;

import com.phoenix.base.cache.StringCacheHandler;
import com.phoenix.base.core.mapper.ArticleMapper;
import com.phoenix.common.enumeration.CachePrefix;
import com.phoenix.base.model.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleManager {
    private final ArticleMapper articleMapper;
    final StringCacheHandler stringCacheHandler;

    private String assembleCacheKey(String id){
        return CachePrefix.BASE_ARTICLE_CONTENT +":"+id;
    }

    public Article selectArticleInCache(String articleId){
        String key = assembleCacheKey(articleId);
        Article article = (Article) stringCacheHandler.getCache(key,Article.class);
        if (article == null){
            article = articleMapper.selectById(articleId);
            stringCacheHandler.set(key, article);
        }
        return article;
    }

    public void deleteArticleInCache(String articleId){
        String key = assembleCacheKey(articleId);
        stringCacheHandler.deleteCache(key);
    }

    public Article selectArticleById(String articleId){
        return articleMapper.selectById(articleId);
    }
}
