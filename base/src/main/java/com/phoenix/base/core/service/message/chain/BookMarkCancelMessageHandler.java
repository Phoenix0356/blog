package com.phoenix.base.core.service.message.chain;

import com.phoenix.base.core.service.MessageService;
import com.phoenix.base.core.service.message.MessageUtil;
import com.phoenix.base.enumeration.MessageType;
import com.phoenix.base.model.dto.MessageDTO;
import com.phoenix.base.model.entity.ArticleData;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMarkCancelMessageHandler implements Handler{
    final MessageService messageService;
    @Override
    public void handle(MessageDTO messageDTO) {
        ArticleData articleData = messageDTO.getArticleData();
        if (MessageUtil.isOptionChosen(messageDTO.getMessageType(),
                MessageType.BOOKMARK_CANCEL.getIdentifier())) {
            articleData.setArticleBookmarkCount(articleData.getArticleBookmarkCount()-1);
            messageService.saveMessage(messageDTO,MessageType.BOOKMARK_CANCEL);
        }
    }

    @Override
    @PostConstruct
    public void registerSelf() {
        MessageChainHandler.handlers.add(this);
    }
}
