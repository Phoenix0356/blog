package com.phoenix.common.dto;


import com.phoenix.common.enumeration.UserStatus;
import lombok.Data;

@Data
public class UserLogDTO {
    String username;
    UserStatus userStatus;
}
