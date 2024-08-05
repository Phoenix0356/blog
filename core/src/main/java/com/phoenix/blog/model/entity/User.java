package com.phoenix.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phoenix.blog.enumeration.Role;
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
@TableName("user")
@Accessors(chain = true)
public class User {
    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    private String userId;

    @TableField("username")
    private String username;

    @TableField("user_password")
    private String password;

    @TableField("user_role")
    private Role userRole;

    @TableField("user_register_time")
    private Timestamp registerTime;

    @TableField("user_avatar_URL")
    private String userAvatarURL;
}
