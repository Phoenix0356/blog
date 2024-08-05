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
@TableName("reply")
@Accessors(chain = true)
public class Reply {

    @TableId(value = "reply_id", type = IdType.ASSIGN_UUID)
    private String replyId;

    @TableField("comment_id")
    private String commentId;

    @TableField("reply_content")
    private String replyContent;

    @TableField("reply_user_name")
    private String replyUserName;

    @TableField("reply_receiver_name")
    private String replyReceiverName;

    @TableField("reply_revise_time")
    private Timestamp replyReviseTime;


}
