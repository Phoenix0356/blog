package com.phoenix.common.dto;

import com.phoenix.common.annotation.FilterEntity;
import com.phoenix.common.annotation.FilterField;
import lombok.Data;

@Data
@FilterEntity
public class ArticleDTO {
    String articleId;
    String articleUserId;

    @FilterField
    String articleTitle;
    @FilterField
    String articleContent;

    String articlePublishTime;

    int articleReadCount;
    int articleUpvoteCountChange;
    int articleBookmarkCountChange;
    int articleMessageType;
}
