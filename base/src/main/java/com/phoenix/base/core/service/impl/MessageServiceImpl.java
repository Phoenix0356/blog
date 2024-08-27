package com.phoenix.base.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.base.core.mapper.ArticleMapper;
import com.phoenix.base.core.mapper.MessageMapper;
import com.phoenix.base.core.service.MessageService;
import com.phoenix.base.enumeration.MessageType;
import com.phoenix.base.model.entity.Article;
import com.phoenix.base.model.entity.ArticleMessage;
import com.phoenix.base.model.vo.ArticleMessageVO;
import com.phoenix.base.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    final MessageMapper messageMapper;
    final ArticleMapper articleMapper;

    @Override
    public ArticleMessageVO getMessageByMessageId(String messageId) {
        ArticleMessage articleMessage = messageMapper.selectById(messageId);
        Article article = articleMapper.selectById(articleMessage.getMessageId());
//        User user = userMapper.selectById(articleMessage.getMessageProducerId());
        return ArticleMessageVO.buildVO(article.getArticleTitle(), articleMessage);
    }

    @Override
    public List<ArticleMessageVO> getMessageListByReceiverId(String receiverId) {
        return messageMapper.getMessageList(receiverId);
    }

    @Override
    public void confirmMessage(String receiverId, int messageTypeNum) {

        if (DataUtil.isOptionChosen(messageTypeNum,MessageType.UPVOTE.getTypeNum())){
            messageMapper.savePulledMessage(receiverId,MessageType.UPVOTE.name());
        }

        if (DataUtil.isOptionChosen(messageTypeNum,MessageType.BOOKMARK.getTypeNum())||
                DataUtil.isOptionChosen(messageTypeNum,MessageType.BOOKMARK_CANCEL.getTypeNum())
        ){
            messageMapper.savePulledMessage(receiverId,MessageType.BOOKMARK.name());
            messageMapper.savePulledMessage(receiverId,MessageType.BOOKMARK_CANCEL.name());
        }

    }

    @Override
    @Async("asyncServiceExecutor")
    public void saveMessage(String messageRelatedArticleId, MessageType messageType, String producerId, String receiverId) {
        if (Objects.equals(producerId, receiverId)) return;
        Article article = articleMapper.selectById(messageRelatedArticleId);
        ArticleMessage articleMessage = messageMapper.selectOne(new QueryWrapper<ArticleMessage>()
                        .eq("message_producer_id",producerId)
                        .eq("message_receiver_id",article.getArticleUserId())
                        .eq("message_related_article_id",article.getArticleId())
                        .eq("message_type",messageType)
                        .orderByDesc("message_generate_time")
                        .last("Limit 1")
        );
        if (articleMessage == null||articleMessage.isMessageIsPulled()) {
            articleMessage = new ArticleMessage();
            articleMessage.setMessageProducerId(producerId)
                    .setMessageReceiverId(receiverId)
                    .setMessageRelatedArticleId(messageRelatedArticleId)
                    .setMessageType(messageType)
                    .setMessageIsPulled(false)
                    .setMessageGenerateTime(new Timestamp(System.currentTimeMillis()));
            messageMapper.insert(articleMessage);
        }else {
            articleMessage.setMessageGenerateTime(new Timestamp(System.currentTimeMillis()));
            messageMapper.updateById(articleMessage);
        }
    }
}
