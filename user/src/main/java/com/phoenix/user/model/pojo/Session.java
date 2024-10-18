package com.phoenix.user.model.pojo;

import com.phoenix.common.enumeration.Role;
import lombok.Data;

@Data
public class Session {
    String userId;
    Role role;
}
