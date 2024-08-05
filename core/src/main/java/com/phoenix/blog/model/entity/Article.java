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

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("article")
@Accessors(chain = true)
public class Article{

    @TableId(value = "article_id", type = IdType.ASSIGN_UUID)
    private String articleId;

    @TableField("article_title")
    private String articleTitle;

    @TableField("article_content")
    private String articleContent;

    @TableField("article_revise_time")
    private Timestamp articleReviseTime;

    @TableField("article_user_id")
    private String articleUserId;

}
