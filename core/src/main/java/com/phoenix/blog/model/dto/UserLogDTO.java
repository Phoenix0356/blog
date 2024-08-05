package com.phoenix.blog.model.dto;

import com.phoenix.blog.enumeration.UserStatus;
import lombok.Data;

@Data
public class UserLogDTO {
    String username;
    UserStatus userStatus;
}
