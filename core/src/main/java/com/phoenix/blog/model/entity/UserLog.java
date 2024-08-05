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
@TableName("user_log")
@Accessors(chain = true)
public class UserLog {
    @TableId(value = "user_log_id", type = IdType.ASSIGN_UUID)
    String userLogId;

    @TableField(value = "user_id")
    String userId;

    @TableField(value = "username")
    String username;

    @TableField(value = "user_login_time")
    Timestamp userLoginTime;

//    @TableField(value = "user_logout_time")
//    Timestamp userLogoutTime;

//    @TableField(value = "user_status")
//    UserStatus userStatus;

}
