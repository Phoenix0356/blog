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
@TableName("filter_record")
@Accessors(chain = true)
public class FilterRecord {
    @TableId(value = "record_id", type = IdType.ASSIGN_UUID)
    private String recordId;

    @TableField("record_username")
    private String recordUsername;

    @TableField("record_user_id")
    private String recordUserId;

    @TableField("record_ip")
    private String recordIP;
}
