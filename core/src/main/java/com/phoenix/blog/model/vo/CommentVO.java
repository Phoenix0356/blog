package com.phoenix.blog.model.vo;

import com.phoenix.blog.model.entity.Comment;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CommentVO {
    String commentId;
    String commentContent;
    int commentUpvoteCount;
    String commentReviseTime;

    String username;
    String userAvatarURL;

    public static CommentVO buildVO(Comment comment){
        return CommentVO.builder()
                .commentId(comment.getCommentId())
                .commentContent(comment.getCommentContent())
                .commentUpvoteCount(comment.getCommentUpvoteCount())
                .commentReviseTime(comment.getCommentReviseTime().toString())
                .build();
    }
}
