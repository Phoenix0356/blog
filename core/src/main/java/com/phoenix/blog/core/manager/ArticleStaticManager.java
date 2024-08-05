package com.phoenix.blog.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.core.mapper.ArticleStaticMapper;
import com.phoenix.blog.model.entity.ArticleStatic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ArticleStaticManager {
    final ArticleStaticMapper articleStaticMapper;

    public ArticleStatic selectByArticleId(String articleId){
        return articleStaticMapper.selectOne(new QueryWrapper<ArticleStatic>()
                .eq("article_id",articleId));
    }

    public void update(ArticleStatic articleStatic){
        articleStaticMapper.updateById(articleStatic);
    }
    public void insert(ArticleStatic articleStatic){
        articleStaticMapper.insert(articleStatic);
    }

    public void deleteByArticleId(ArticleStatic articleStatic){
        articleStaticMapper.deleteById(articleStatic);
    }
}
