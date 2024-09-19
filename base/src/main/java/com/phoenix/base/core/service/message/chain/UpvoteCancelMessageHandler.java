package com.phoenix.base.core.service.message.chain;

import com.phoenix.base.core.manager.ArticleUpVoteManager;
import com.phoenix.base.core.service.MessageService;
import com.phoenix.base.core.service.message.MessageUtil;
import com.phoenix.base.enumeration.MessageType;
import com.phoenix.base.model.dto.MessageDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpvoteCancelMessageHandler implements Handler{
    final ArticleUpVoteManager articleUpVoteManager;
    final MessageService messageService;
    @Override
    public void handle(MessageDTO messageDTO) {
        if (MessageUtil.isOptionChosen(messageDTO.getMessageType(),
                MessageType.UPVOTE_CANCEL.getIdentifier())) {
            articleUpVoteManager.setUserIntoCache(messageDTO.getArticleUserId(), messageDTO.getArticleId());
            messageService.saveMessage(messageDTO,MessageType.UPVOTE_CANCEL);
        }
    }

    @Override
    @PostConstruct
    public void registerSelf() {
        MessageChainHandler.handlers.add(this);
    }
}
