package com.phoenix.blog.core.service;

import com.phoenix.blog.interfaces.FieldsInjector;
import com.phoenix.blog.model.dto.ArticleDTO;
import com.phoenix.blog.model.vo.ArticleVO;

import java.util.List;


public interface ArticleService {

    ArticleVO getArticleById(String articleId);
    List<ArticleVO> getArticleAll(int sortStrategy);
    List<ArticleVO> getArticleUserList(String userId);
    void SaveArticleByUser(ArticleDTO articleDTO);
    void updateArticleContent(ArticleDTO articleDTO);
    void updateArticleStatics(ArticleDTO articleDTO);
    void deleteArticleBookmarkCount(String articleId);
    void deleteArticleById(String articleId);
}
