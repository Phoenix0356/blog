package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.ReplyDTO;
import com.phoenix.blog.model.vo.ReplyVO;


public interface ReplyService {
    public ReplyVO getReplyById(String replyId);

    public ReplyVO getCommentReplyList(String commentId);

    public void saveReply(ReplyDTO replyDTO);

    public void updateReply(ReplyDTO replyDTO);

    public void deleteReply(String replyId);

}
