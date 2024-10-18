package com.phoenix.user.model.vo;

import com.phoenix.user.model.entity.User;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserVO {
    String username;
    String userAvatarURL;
    int roleLevel;

    public static UserVO BuildVO(User user) {
        return UserVO.builder()
                .username(user.getUsername())
                .userAvatarURL(user.getUserAvatarURL())
                .roleLevel(user.getUserRole().getLevel())
                .build();
    }

}
