package com.phoenix.user.core.service;

import com.phoenix.user.model.vo.UserVO;
import com.phoenix.common.dto.UserDTO;
import com.phoenix.common.dto.UserLoginDTO;
import com.phoenix.common.dto.UserRegisterDTO;

public interface UserService {
    UserVO register(UserRegisterDTO userRegisterDTO,String sessionId);

    UserVO login(UserLoginDTO userLoginDTO,String sessionId);

    UserVO getCurUser(String userId);

    UserVO getUserByUserId(String userId);

    UserVO updateUser(UserDTO userDTO,String userId);

    void logout(String sessionId);



}
