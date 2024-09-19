package com.phoenix.base.model.dto;

import com.phoenix.base.enumeration.MessageType;
import com.phoenix.base.model.entity.Article;
import com.phoenix.base.model.entity.ArticleData;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageDTO {
    String articleId;
    int messageType;
    String operatorUserId;
    String articleUserId;
    ArticleData articleData;
}
