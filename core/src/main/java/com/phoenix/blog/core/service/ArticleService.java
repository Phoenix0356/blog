package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.ArticleDTO;
import com.phoenix.blog.model.vo.ArticleVO;

import java.util.List;


public interface ArticleService {

    ArticleVO getArticleDetailById(String articleId);
    List<ArticleVO> getArticleAll(int sortStrategy);
    List<ArticleVO> getArticleUserList(String userId);
    ArticleVO saveArticleByUser(ArticleDTO articleDTO);
    void updateArticleContent(ArticleDTO articleDTO);
    void updateArticleStatics(ArticleDTO articleDTO);
    void deleteArticleBookmarkCount(String articleId);
    void deleteArticleById(String articleId);
}
