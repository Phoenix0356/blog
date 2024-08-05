package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.core.mapper.ArticleMapper;
import com.phoenix.blog.core.mapper.MessageMapper;
import com.phoenix.blog.core.mapper.UserMapper;
import com.phoenix.blog.core.service.MessageService;
import com.phoenix.blog.enumeration.MessageType;
import com.phoenix.blog.model.entity.Article;
import com.phoenix.blog.model.entity.ArticleMessage;
import com.phoenix.blog.model.entity.User;
import com.phoenix.blog.model.vo.ArticleMessageVO;
import com.phoenix.blog.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    final MessageMapper messageMapper;
    final ArticleMapper articleMapper;
    final UserMapper userMapper;

    @Override
    public ArticleMessageVO getMessageByMessageId(String messageId) {
        ArticleMessage articleMessage = messageMapper.selectById(messageId);
        Article article = articleMapper.selectById(articleMessage.getMessageId());
        User user = userMapper.selectById(articleMessage.getMessageProducerId());
        return ArticleMessageVO.buildVO(user.getUsername(),article.getArticleTitle(), articleMessage);
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
    public void saveMessage(String messageRelatedArticleId,MessageType messageType, String producerId) {
        Article article = articleMapper.selectById(messageRelatedArticleId);
        User receiveUser = userMapper.selectById(article.getArticleUserId());

        //如果自己操作自己文章，直接返回
        if (receiveUser.getUserId().equals(producerId)){
            return;
        }

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
                    .setMessageReceiverId(receiveUser.getUserId())
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
