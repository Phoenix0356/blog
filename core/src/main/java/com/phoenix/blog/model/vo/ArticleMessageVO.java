package com.phoenix.blog.model.vo;

import com.phoenix.blog.model.entity.ArticleMessage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleMessageVO {
    String messageId;
    boolean messageIsPulled;
    String messageType;
    String messageGenerateTime;

    String messageProducerUsername;
    String messageRelatedArticleName;

    public static ArticleMessageVO buildVO(String messageProducerUsername,
                                           String messageRelatedArticleName,
                                           ArticleMessage articleMessage) {
        return ArticleMessageVO.builder()
                .messageId(articleMessage.getMessageId())
                .messageProducerUsername(messageProducerUsername)
                .messageRelatedArticleName(messageRelatedArticleName)
                .messageType(articleMessage.getMessageType().name())
                .messageIsPulled(articleMessage.isMessageIsPulled())
                .messageGenerateTime(articleMessage.getMessageGenerateTime().toString())
                .build();
    }
}
