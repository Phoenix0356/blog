package com.phoenix.filter.entity;

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
@TableName("word")
@Accessors(chain = true)
public class SensitiveWord {
    @TableId(value = "word_id", type = IdType.ASSIGN_ID)
    private String wordId;

    @TableField("word_string")
    private String wordString;
}
