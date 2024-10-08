package com.phoenix.base.core.service.data.chain;

import com.phoenix.base.core.manager.ArticleUpVoteManager;
import com.phoenix.base.core.service.MessageService;
import com.phoenix.base.enumeration.DataStateType;
import com.phoenix.base.model.dto.MessageDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpVoteStateHandler implements Handler {
    final ArticleUpVoteManager articleUpVoteManager;
    final MessageService messageService;
    @Override
    public void handle(MessageDTO messageDTO) {
        int upvoteState = messageDTO.getArticleDataState()&DataStateType.UPVOTE.getIdentifier();
        int upvoteChangedState = messageDTO.getArticleDataChangedState()&DataStateType.UPVOTE.getIdentifier();

        if (upvoteState == upvoteChangedState) {
            return;
        }

        if (upvoteChangedState == 1) {
            articleUpVoteManager.addUpvoteUser(messageDTO.getArticleId(),messageDTO.getOperatorUserId());
            messageService.saveMessage(messageDTO, DataStateType.UPVOTE);
        }else{
            articleUpVoteManager.deleteUpvoteUser(messageDTO.getArticleId(), messageDTO.getOperatorUserId());
            messageService.saveMessage(messageDTO, DataStateType.UPVOTE_CANCEL);
        }


    }

    @Override
    @PostConstruct
    public void registerSelf() {
        DataChangeChainHandler.handlers.add(this);
    }
}
