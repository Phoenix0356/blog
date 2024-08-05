package com.phoenix.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phoenix.blog.model.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("comment")
@Accessors(chain = true)
public class Comment {

    @TableId(value = "comment_id", type = IdType.ASSIGN_UUID)
    private String commentId;

    @TableField("comment_content")
    private String commentContent;

    @TableField("comment_upvote_count")
    private int commentUpvoteCount;

    @TableField("comment_revise_time")
    private Timestamp commentReviseTime;

    @TableField("comment_user_id")
    private String commentUserId;

    @TableField("comment_article_id")
    private String commentArticleId;

}
