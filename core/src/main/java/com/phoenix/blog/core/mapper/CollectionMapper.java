package com.phoenix.blog.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.model.entity.Collection;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.model.vo.CollectionVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {

    List<CollectionVO> selectCollectionsByUsername(String username);
    List<ArticleVO> selectCollectionArticleList(String collectionId);

    void insertArticleIntoCollection(Map<String,String> map);

    void updateArticleNoteIntoCollection(String collectionId, String articleId, String remark);

    void deleteArticleFromCollection(String collectionId,String articleId);

    int isArticleExistsInCollection(String collectionId,String articleId);
}
