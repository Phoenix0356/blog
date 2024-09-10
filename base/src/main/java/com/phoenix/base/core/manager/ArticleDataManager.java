package com.phoenix.base.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.base.core.mapper.ArticleDataMapper;
import com.phoenix.base.model.entity.ArticleData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDataManager {
    final ArticleDataMapper articleDataMapper;

    public ArticleData selectByArticleId(String articleId){
        return articleDataMapper.selectOne(new QueryWrapper<ArticleData>()
                .eq("article_id",articleId));
    }

    public void update(ArticleData articleData){articleDataMapper.updateById(articleData);}
    public void insert(ArticleData articleData){
        articleDataMapper.insert(articleData);
    }

    public void deleteByArticleId(String articleId){
        articleDataMapper.deleteById(articleId);
    }
}
