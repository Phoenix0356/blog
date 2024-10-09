package com.phoenix.base.model.vo;

import com.phoenix.base.model.entity.Article;
import com.phoenix.base.model.entity.ArticleData;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ArticleVO {
    String articleId;
    String articleTitle;
    String articleContent;
    String articleUserId;
    String articleReviseTime;
    int articleUpvoteCount;
    int articleReadCount;
    int articleBookmarkCount;
    int articleDataState;
//    String username;
//    String userAvatarURL;
    String collectionArticleNote;
    public static ArticleVO buildVO(Article article, ArticleData articleData){
        return new ArticleVO()
                .setArticleId(article.getArticleId())
                .setArticleTitle(article.getArticleTitle())
                .setArticleContent(article.getArticleContent())
                .setArticleReviseTime(article.getArticleReviseTime().toString())
                .setArticleUserId(article.getArticleUserId())
                .setArticleReadCount(articleData.getArticleReadCount());
    }
}
