package com.phoenix.base.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.base.model.entity.Collection;
import com.phoenix.base.model.vo.ArticleVO;
import com.phoenix.base.model.vo.CollectionVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {
    List<ArticleVO> selectCollectionArticleList(String collectionId);

    void insertArticleIntoCollection(Map<String,String> map);

    void updateArticleNoteIntoCollection(String collectionId, String articleId, String remark);

    void deleteArticleFromCollection(String collectionId,String articleId);

    int isArticleExistsInCollection(String collectionId,String articleId);
}
