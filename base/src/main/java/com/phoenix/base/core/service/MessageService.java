package com.phoenix.base.core.service;

import com.phoenix.base.enumeration.MessageType;
import com.phoenix.base.model.vo.ArticleMessageVO;

import java.util.List;

public interface MessageService {
    ArticleMessageVO getMessageByMessageId(String messageId);
    List<ArticleMessageVO> getMessageListByReceiverId(String receiverId);
    void saveMessage(String messageRelatedArticleId, MessageType messageType, String producerId, String receiverId);

    void confirmMessage(String userId,int messageTypeNum);
}
