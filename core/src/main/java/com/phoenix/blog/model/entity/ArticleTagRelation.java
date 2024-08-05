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
@TableName("article_tag_list")
@Accessors(chain = true)
public class ArticleTagRelation {
    @TableId(value = "article_tag_list_id", type = IdType.ASSIGN_UUID)
    private String articleTagListId;

    @TableField(value = "article_id")
    private String articleId;

    @TableField(value = "tag_id")
    private String TagId;
}
