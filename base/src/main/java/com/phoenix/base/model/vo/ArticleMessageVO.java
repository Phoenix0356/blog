package com.phoenix.base.model.vo;


import com.phoenix.base.model.entity.ArticleMessage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleMessageVO {
    String messageId;
    boolean messageIsPulled;
    String messageType;
    String messageGenerateTime;

//    String messageProducerUsername;
    String messageRelatedArticleName;

    public static ArticleMessageVO buildVO(String messageRelatedArticleName,
                                           ArticleMessage articleMessage) {
        return ArticleMessageVO.builder()
                .messageId(articleMessage.getMessageId())
                .messageRelatedArticleName(messageRelatedArticleName)
                .messageType(articleMessage.getMessageType().name())
                .messageIsPulled(articleMessage.isMessageIsPulled())
                .messageGenerateTime(articleMessage.getMessageGenerateTime().toString())
                .build();
    }
}
