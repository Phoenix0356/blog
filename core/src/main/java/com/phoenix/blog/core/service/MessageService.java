package com.phoenix.blog.core.service;

import com.phoenix.blog.enumeration.MessageType;
import com.phoenix.blog.model.vo.ArticleMessageVO;

import java.util.List;

public interface MessageService {
    ArticleMessageVO getMessageByMessageId(String messageId);
    List<ArticleMessageVO> getMessageListByReceiverId(String receiverId);
    void saveMessage(String messageRelatedArticleId, MessageType messageType, String producerId);

    void confirmMessage(String userId,int messageTypeNum);
}
