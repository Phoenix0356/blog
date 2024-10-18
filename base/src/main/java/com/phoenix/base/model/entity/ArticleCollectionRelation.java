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
@TableName("collection_article_list")
@Accessors(chain = true)
public class ArticleCollectionRelation {
    @TableId(value = "collection_article_list_id", type = IdType.AUTO)
    private Integer collectionArticleListId;

    @TableField(value = "collection_id")
    private String collectionId;

    @TableField(value = "article_id")
    private String articleId;

    @TableField(value = "collection_article_note")
    private String collectionArticleNote;
}
