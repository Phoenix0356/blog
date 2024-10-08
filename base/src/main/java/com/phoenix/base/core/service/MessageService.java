package com.phoenix.base.core.service;

import com.phoenix.base.enumeration.DataStateType;
import com.phoenix.base.model.vo.ArticleMessageVO;
import com.phoenix.base.model.dto.MessageDTO;

import java.util.List;

public interface MessageService {
    ArticleMessageVO getMessageByMessageId(String messageId);
    List<ArticleMessageVO> getMessageListByReceiverId(String receiverId);
    void saveMessage(MessageDTO messageDTO, DataStateType dataStateType);

    void confirmMessage(String userId,int messageTypeNum);
}
