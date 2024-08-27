package com.phoenix.base.core.service;

import com.phoenix.common.dto.ArticleNoteDTO;
import com.phoenix.common.dto.CollectionAddDTO;
import com.phoenix.common.dto.CollectionDTO;
import com.phoenix.base.model.vo.ArticleVO;
import com.phoenix.base.model.vo.CollectionVO;

import java.util.List;

public interface CollectionService {
    CollectionVO getCollection(String collectionId);
    List<CollectionVO> getAllCollections();
    List<ArticleVO> getAllArticleList(String collectionId);

    void saveArticleIntoCollection(CollectionAddDTO collectionAddDTO, String userId);

    void saveNoteIntoArticle(String articleId, ArticleNoteDTO articleNoteDTO);

    void saveCollection(CollectionDTO collectionDTO, String userId);

    void updateCollection(CollectionDTO collectionDTO);
    void deleteArticleFromCollect(String collectionId, String articleId);

    void deleteCollectionById(String collectionId);


}
