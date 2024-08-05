package com.phoenix.blog.model.dto;

import lombok.Data;

@Data
public class ArticleMessageDTO {
    String messageType;
    String messageRelatedArticleId;
}
