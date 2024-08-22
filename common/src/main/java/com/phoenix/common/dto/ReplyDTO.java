package com.phoenix.common.dto;

import lombok.Data;

@Data
public class ReplyDTO {
    String replyId;
    String replyCommentId;
    String replyUsername;
    String replyReceiverName;
    String replyContent;

}
