package com.phoenix.blog.model.dto;

import com.phoenix.blog.enumeration.Role;
import lombok.Data;

@Data
public class UserRegisterDTO {
    String username;
    String password;
    Role role = Role.MEMBER;
    String avatarBase64;
}
