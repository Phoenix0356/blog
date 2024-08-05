package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.UserDTO;
import com.phoenix.blog.model.dto.UserLoginDTO;
import com.phoenix.blog.model.dto.UserRegisterDTO;
import com.phoenix.blog.model.vo.UserVO;

import java.util.Date;

public interface UserService {
    public UserVO register(UserRegisterDTO userRegisterDTO);

    public UserVO login(UserLoginDTO userLoginDTO);

    public UserVO getUserById(String userId);

    public UserVO getUserByUsername(String username);

    public UserVO updateUser(UserDTO userDTO,String userId);

    public void logout(String jwtId, String userId, Date jwtExpirationTime);



}
