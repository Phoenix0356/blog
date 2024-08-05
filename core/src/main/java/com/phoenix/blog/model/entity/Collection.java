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
@TableName("collection")
@Accessors(chain = true)
public class Collection {
    @TableId(value = "collection_id",type = IdType.ASSIGN_UUID)
    public String collectionId;

    @TableField(value = "collection_user_id")
    public String collectionUserId;

    @TableField(value = "collection_name")
    public String collectionName;

    @TableField(value = "collection_revise_time")
    public Timestamp collectionReviseTime;

    @TableField(value = "collection_description")
    public String collectionDescription;
}
