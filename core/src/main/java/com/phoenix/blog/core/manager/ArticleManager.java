package com.phoenix.blog.core.manager;

import com.phoenix.blog.cache.RedisCacheHandler;
import com.phoenix.blog.core.mapper.ArticleMapper;
import com.phoenix.blog.model.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleManager {
    private final ArticleMapper articleMapper;
    final RedisCacheHandler redisCacheHandler;

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
