package com.phoenix.base.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.base.core.controller.ArticleController;
import com.phoenix.base.core.mapper.ArticleCollectionMapper;
import com.phoenix.base.core.mapper.CollectionMapper;
import com.phoenix.base.core.service.CollectionService;
import com.phoenix.base.model.entity.ArticleCollectionRelation;
import com.phoenix.base.model.entity.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CollectionManager {
    private final CollectionMapper collectionMapper;
    private final ArticleCollectionMapper articleCollectionMapper;

    public Collection select(String collectionId) {
        return collectionMapper.selectById(collectionId);
    }

    /*
    * 查询用户是否收藏了这篇文章
    * 从收藏夹表中通过userId查询出用户的收藏夹，然后扫描所有收藏夹下是否有该articleId
    * */
    public boolean isArticleCollectedByUser(String userId, String articleId){
        //查出用户的收藏列表
        List<Collection> collectionList = collectionMapper.selectList(new QueryWrapper<Collection>().
                eq("collection_user_id", userId));
        //遍历列表，查找是否存在目标文章
        for(Collection collection : collectionList){
            if(collectionMapper.isArticleExistsInCollection(collection.getCollectionId(),articleId) == 1) {
                return true;
            }
        }
        return false;
    }

    public int getArticleCollectedCount(String articleId){
        List<ArticleCollectionRelation> articleCollectionRelationList = articleCollectionMapper
                .selectList(new QueryWrapper<ArticleCollectionRelation>()
                .eq("article_id", articleId));
        return articleCollectionRelationList.size();
    }

    public void insertArticleCollectionManager(ArticleCollectionRelation articleCollectionRelation){
        int result = articleCollectionMapper.insert(articleCollectionRelation);
        if(result != 1){
            log.error("插入收藏夹ID为 {} ，文章ID为 {} 的关系对象失败", articleCollectionRelation.getArticleId()
                    , articleCollectionRelation.getCollectionId());
        }
    }
}
