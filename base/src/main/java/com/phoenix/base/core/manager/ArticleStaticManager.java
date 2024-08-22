package com.phoenix.base.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.base.core.mapper.ArticleStaticMapper;
import com.phoenix.base.model.entity.ArticleData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleStaticManager {
    final ArticleStaticMapper articleStaticMapper;

    public ArticleData selectByArticleId(String articleId){
        return articleStaticMapper.selectOne(new QueryWrapper<ArticleData>()
                .eq("article_id",articleId));
    }

    public void update(ArticleData articleStatic){
        articleStaticMapper.updateById(articleStatic);
    }
    public void insert(ArticleData articleStatic){
        articleStaticMapper.insert(articleStatic);
    }

    public void deleteByArticleId(ArticleData articleStatic){
        articleStaticMapper.deleteById(articleStatic);
    }
}
