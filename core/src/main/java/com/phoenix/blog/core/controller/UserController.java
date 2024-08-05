package com.phoenix.blog.core.controller;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.constant.RespMessageConstant;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.service.UserService;
import com.phoenix.blog.enumeration.Role;
import com.phoenix.blog.model.dto.UserDTO;
import com.phoenix.blog.model.dto.UserLoginDTO;
import com.phoenix.blog.model.dto.UserRegisterDTO;
import com.phoenix.blog.model.vo.ResultVO;
import com.phoenix.blog.model.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;
    @GetMapping("/get")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getUser(){
        UserVO userVO;
        userVO = userService.getUserById(TokenContext.getUserId());

        return ResultVO.success(RespMessageConstant.GET_SUCCESS,userVO);
    }

    @PutMapping("/update")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO updateUser(@RequestBody UserDTO userDTO){
        UserVO userVO = userService.updateUser(userDTO,TokenContext.getUserId());
        return ResultVO.success(RespMessageConstant.UPDATE_SUCCESS,userVO);
    }
    @PostMapping("/register")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO register(@RequestBody UserRegisterDTO userRegisterDTO){
        UserVO userVO = userService.register(userRegisterDTO);
        return ResultVO.success(RespMessageConstant.REGISTER_SUCCESS,userVO);
    }
    @PostMapping("/login")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO login(@RequestBody UserLoginDTO userLoginDTO){
        UserVO userVO = userService.login(userLoginDTO);
        return ResultVO.success(RespMessageConstant.LOGIN_SUCCESS,userVO);
    }

    @PostMapping("/logout")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO logout(){
        userService.logout(TokenContext.getJti(),TokenContext.getUserId(),TokenContext.getExpirationTime());
        return ResultVO.success(RespMessageConstant.LOGOUT_SUCCESS);
    }
}
