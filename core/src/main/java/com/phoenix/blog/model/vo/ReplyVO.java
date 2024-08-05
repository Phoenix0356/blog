package com.phoenix.blog.model.vo;

import com.phoenix.blog.model.entity.Reply;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReplyVO {
    String replyId;
    String replyUsername;
    String replyReceiverName;
    String replyContent;

    String username;

    public static ReplyVO buildVO(Reply reply){
        return ReplyVO.builder()
                .replyId(reply.getReplyId())
                .replyUsername(reply.getReplyUserName())
                .replyReceiverName(reply.getReplyReceiverName())
                .replyContent(reply.getReplyContent()).build();
    }

}
