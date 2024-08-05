package com.phoenix.blog.model.dto;

import lombok.Data;

@Data
public class ArticleDTO {
    String articleId;
    String articleUserId;

    String articleTitle;
    String articleContent;
    String articlePublishTime;

    int articleReadCount;
    int articleUpvoteCountChange;
    int articleBookmarkCountChange;
    int articleMessageType;
}
