package com.phoenix.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phoenix.blog.enumeration.MessageType;
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
@TableName("message_article")
@Accessors(chain = true)
public class ArticleMessage {

    @TableId(value = "message_id", type = IdType.ASSIGN_UUID)
    private String messageId;

    @TableField("message_producer_id")
    private String messageProducerId;

    @TableField("message_receiver_id")
    private String messageReceiverId;

    @TableField("message_type")
    private MessageType messageType;

    @TableField("message_related_article_id")
    private String messageRelatedArticleId;

    @TableField("message_is_pulled")
    private boolean messageIsPulled;

    @TableField("message_generate_time")
    private Timestamp messageGenerateTime;



}

