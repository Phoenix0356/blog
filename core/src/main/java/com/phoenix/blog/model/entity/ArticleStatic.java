package com.phoenix.blog.model.entity;

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
@TableName("article_static")
@Accessors(chain = true)
public class ArticleStatic {

    @TableId(value = "articled_static_id", type = IdType.ASSIGN_UUID)
    private String articleStaticId;

    @TableField("article_read_count")
    private int articleReadCount;

    @TableField("article_upvote_count")
    private int articleUpvoteCount;

    @TableField("article_bookmark_count")
    private int articleBookmarkCount;

    @TableField("article_id")
    private String articleId;

}
