package com.phoenix.common.dto;

import lombok.Data;

@Data
public class ArticleMessageDTO {
    String messageType;
    String messageRelatedArticleId;
}
