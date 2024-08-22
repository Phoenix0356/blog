package com.phoenix.base.core.service.impl;

import com.phoenix.base.core.mapper.ReplyMapper;
import com.phoenix.base.core.service.ReplyService;
import com.phoenix.common.dto.ReplyDTO;
import com.phoenix.base.model.entity.Reply;
import com.phoenix.base.model.vo.ReplyVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    final private ReplyMapper replyMapper;


    @Override
    public ReplyVO getReplyById(String replyId) {
        Reply reply = replyMapper.selectById(replyId);
        return ReplyVO.buildVO(reply);
    }

    @Override
    public ReplyVO getCommentReplyList(String commentId) {

        return null;
    }

    @Override
    public void saveReply(ReplyDTO replyDTO) {
        Reply reply = new Reply();

        reply.setReplyUserName(replyDTO.getReplyUsername())
                .setReplyReceiverName(replyDTO.getReplyReceiverName())
                .setReplyContent(replyDTO.getReplyContent())
                .setReplyReviseTime(new Timestamp(System.currentTimeMillis()));

        replyMapper.insert(reply);
    }

    @Override
    public void updateReply(ReplyDTO replyDTO) {
        Reply reply = new Reply();

        reply.setReplyContent(replyDTO.getReplyContent())
                .setReplyReviseTime(new Timestamp(System.currentTimeMillis()));

        replyMapper.updateById(reply);
    }

    @Override
    public void deleteReply(String replyId) {
        replyMapper.deleteById(replyId);
    }
}
