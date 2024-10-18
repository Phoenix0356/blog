package com.phoenix.base.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("article_upvote")
@Accessors(chain = true)
public class ArticleUpvoteRelation {
    @TableId(value = "article_upvote_id", type = IdType.AUTO)
    private Integer articleUpvoteId;

    @TableField("article_id")
    private String articleId;

    @TableField("upvote_user_id")
    private String upvoteUserId;

}
