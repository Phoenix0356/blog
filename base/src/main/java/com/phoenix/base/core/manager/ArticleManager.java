package com.phoenix.base.core.manager;

import com.phoenix.base.cache.StringCacheHandler;
import com.phoenix.base.core.mapper.ArticleMapper;
import com.phoenix.base.model.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleManager {
    private final ArticleMapper articleMapper;
    final StringCacheHandler stringCacheHandler;

    //从redis缓存中拿数据，如果没有就去数据库中取，再更新到缓存中
    public Article selectArticleInCache(String articleId){
        Article article = (Article) stringCacheHandler.getCache(articleId,Article.class);
        if (article == null){
            article = articleMapper.selectById(articleId);
            stringCacheHandler.set(articleId, article);
        }
        return article;
    }

    public void deleteArticleInCache(String articleId){
        stringCacheHandler.deleteCache(articleId);
    }

}
