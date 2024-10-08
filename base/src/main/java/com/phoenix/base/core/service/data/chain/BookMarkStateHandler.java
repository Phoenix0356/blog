package com.phoenix.base.core.service.data.chain;

import com.phoenix.base.core.service.MessageService;
import com.phoenix.base.enumeration.DataStateType;
import com.phoenix.base.model.dto.MessageDTO;
import com.phoenix.base.model.entity.ArticleData;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMarkStateHandler implements Handler{
    final MessageService messageService;
    @Override
    public void handle(MessageDTO messageDTO) {
        ArticleData articleData = messageDTO.getArticleData();
        //提取目标二进制位
        int bookmarkState = messageDTO.getArticleDataState()&DataStateType.BOOKMARK.getIdentifier();
        int bookmarkChangedState = messageDTO.getArticleDataState()&DataStateType.BOOKMARK.getIdentifier();

        //如果目标位相等说明没变化，直接返回
        if(bookmarkState==bookmarkChangedState){
            return;
        }
        //如果改变
        if(bookmarkChangedState == 1){
            articleData.setArticleBookmarkCount(articleData.getArticleBookmarkCount()+1);
            messageService.saveMessage(messageDTO, DataStateType.BOOKMARK);
        }else{
            articleData.setArticleBookmarkCount(articleData.getArticleBookmarkCount()-1);
            messageService.saveMessage(messageDTO, DataStateType.BOOKMARK_CANCEL);
        }
    }

    @Override
    @PostConstruct
    public void registerSelf() {
        DataChangeChainHandler.handlers.add(this);
    }
}
