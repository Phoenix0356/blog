package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.ArticleNoteDTO;
import com.phoenix.blog.model.dto.CollectionAddDTO;
import com.phoenix.blog.model.dto.CollectionDTO;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.model.vo.CollectionVO;

import java.util.List;

public interface CollectionService {
    CollectionVO getCollection(String collectionId);
    List<CollectionVO> getAllCollections(String username);
    List<ArticleVO> getAllArticleList(String collectionId);

    void saveArticleIntoCollection(CollectionAddDTO collectionAddDTO, String userId);

    void saveNoteIntoArticle(String articleId, ArticleNoteDTO articleNoteDTO);

    void saveCollection(CollectionDTO collectionDTO, String userId);

    void updateCollection(CollectionDTO collectionDTO);
    void deleteArticleFromCollect(String collectionId, String articleId);

    void deleteCollectionById(String collectionId);


}
