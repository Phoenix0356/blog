package com.phoenix.blog.model.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data

public class CommentDTO {
    String commentId;
    String commentContent;
    int commentUpvoteCount;
    String commentArticleId;
    String commentUserId;
}
