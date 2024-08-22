package com.phoenix.user.core.service;

import com.phoenix.user.model.vo.UserVO;
import com.phoenix.common.dto.UserDTO;
import com.phoenix.common.dto.UserLoginDTO;
import com.phoenix.common.dto.UserRegisterDTO;

import java.util.Date;

public interface UserService {
    public UserVO register(UserRegisterDTO userRegisterDTO);

    public UserVO login(UserLoginDTO userLoginDTO);

    public UserVO getUserById(String userId);

    public UserVO getUserByUsername(String username);

    public UserVO updateUser(UserDTO userDTO,String userId);

    public void logout(String jwtId, String userId, Date jwtExpirationTime);



}
