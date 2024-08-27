package com.phoenix.base.core.service;

import com.phoenix.common.dto.ArticleDTO;
import com.phoenix.base.model.vo.ArticleVO;

import java.util.List;


public interface ArticleService {

    ArticleVO getArticleDetailById(String articleId);
    List<ArticleVO> getArticleAll(int sortStrategy);
    List<ArticleVO> getArticleUserList(String userId);
    ArticleVO saveArticleByUser(ArticleDTO articleDTO);
    void updateArticleContent(ArticleDTO articleDTO);
    void updateArticleData(ArticleDTO articleDTO);
    void deleteArticleBookmarkCount(String articleId);
    void deleteArticleById(String articleId);
}
