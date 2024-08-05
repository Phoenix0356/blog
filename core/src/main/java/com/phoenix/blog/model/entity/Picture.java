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
@TableName("picture")
@Accessors(chain = true)
public class Picture {

    @TableId(value = "picture_id", type = IdType.ASSIGN_UUID)
    private String pictureId;

    @TableField("picture_article_id")
    private String pictureArticleId;

    @TableField("picture_path")
    private String picturePath;

}
