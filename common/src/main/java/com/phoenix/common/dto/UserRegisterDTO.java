package com.phoenix.common.dto;

import com.phoenix.common.annotation.FilterEntity;
import com.phoenix.common.annotation.FilterField;
import com.phoenix.common.enumeration.Role;
import lombok.Data;

@Data
@FilterEntity
public class UserRegisterDTO {
    @FilterField
    String username;
    String password;
    Role role = Role.MEMBER;
    String avatarBase64;
}
