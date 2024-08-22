package com.phoenix.base.core.service;

import com.phoenix.common.dto.ReplyDTO;
import com.phoenix.base.model.vo.ReplyVO;


public interface ReplyService {
    public ReplyVO getReplyById(String replyId);

    public ReplyVO getCommentReplyList(String commentId);

    public void saveReply(ReplyDTO replyDTO);

    public void updateReply(ReplyDTO replyDTO);

    public void deleteReply(String replyId);

}
