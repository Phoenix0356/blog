package com.phoenix.base.core.service;

import com.phoenix.base.model.vo.ArticleDataVO;
import com.phoenix.common.dto.ArticleDTO;
import com.phoenix.base.model.vo.ArticleVO;

import java.util.List;


public interface ArticleService {

    ArticleVO getArticleDetailById(String articleId);
    ArticleDataVO getArticleDataStateById(String articleId);
    List<ArticleVO> getArticleAll(int sortStrategy);
    List<ArticleVO> getArticleUserList(String userId);
    ArticleVO saveArticleByUser(ArticleDTO articleDTO);
    void updateArticleContent(ArticleDTO articleDTO);
    void updateAuthorizedArticleData(ArticleDTO articleDTO);
    void updateCommonArticleData(String articleId);
    void deleteArticleById(String articleId);
}
