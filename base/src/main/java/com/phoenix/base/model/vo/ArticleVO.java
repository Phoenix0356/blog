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
    String articleReviseTime;
    int articleUpvoteCount;
    int articleReadCount;
    int articleBookmarkCount;

//    String username;
//    String userAvatarURL;
//
//    String collectionArticleNote;
public static ArticleVO buildVO(Article article, ArticleData articleStatic){
    return new ArticleVO()
            .setArticleId(article.getArticleId())
            .setArticleTitle(article.getArticleTitle())
            .setArticleContent(article.getArticleContent())
            .setArticleReviseTime(article.getArticleReviseTime().toString())
            .setArticleReadCount(articleStatic.getArticleReadCount())
            .setArticleUpvoteCount(articleStatic.getArticleUpvoteCount())
            .setArticleBookmarkCount(articleStatic.getArticleBookmarkCount());
}
}
