package com.phoenix.base.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.base.core.mapper.ArticleTagMapper;
import com.phoenix.base.model.entity.ArticleTagRelation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleTagManager {

    private final ArticleTagMapper articleTagMapper;

    public ArticleTagRelation select(String articleId, String tagId) {
        return articleTagMapper.selectOne(new QueryWrapper<ArticleTagRelation>()
                .eq("article_id",articleId)
                .eq("tag_id",tagId)
        );
    }

    public List<ArticleTagRelation> selectListByArticleId(String articleId){
        return articleTagMapper.selectList(new QueryWrapper<ArticleTagRelation>()
                .eq("article_id",articleId)
        );
    }

    public List<ArticleTagRelation> selectListByTagId(String tagId){
        return articleTagMapper.selectList(new QueryWrapper<ArticleTagRelation>()
                .eq("tag_id",tagId)
        );
    }
    public void insert(ArticleTagRelation articleTagRelation){
        int result = articleTagMapper.insert(articleTagRelation);
        if (result == 0){
            log.error("ID为 {} 的文章-标签关系插入数据库失败",articleTagRelation.getArticleTagListId());
        }
    }

    public void delete(ArticleTagRelation articleTagRelation){
        int result = articleTagMapper.deleteById(articleTagRelation);
        if (result == 0){
            log.error("ID为 {} 的文章-标签关系从数据库删除失败",articleTagRelation.getArticleTagListId());
        }
    }

    public void deleteBatchByIds(List<String> idList){
        if (idList.isEmpty()) return;
        int result = articleTagMapper.deleteBatchIds(idList);
        if (result == 0){
            log.error("文章-标签关系列表从数据库删除失败");
        }
    }
    public void deleteBatch(List<ArticleTagRelation>  articleTagRelationList){
        List<String> idList = articleTagRelationList.stream()
                .map(ArticleTagRelation::getArticleTagListId)
                .toList();
        if (idList.isEmpty()) return;
        int result = articleTagMapper.deleteBatchIds(idList);
        if (result == 0){
            log.error("文章-标签关系列表从数据库删除失败");
        }
    }
}
