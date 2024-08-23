package com.phoenix.user.core.controller;

import com.phoenix.user.context.TokenContext;
import com.phoenix.user.core.service.UserService;
import com.phoenix.common.vo.ResultVO;
import com.phoenix.user.model.vo.UserVO;
import com.phoenix.common.annotation.AuthorizationRequired;
import com.phoenix.common.annotation.FilterNeeded;
import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.common.dto.UserDTO;
import com.phoenix.common.dto.UserLoginDTO;
import com.phoenix.common.dto.UserRegisterDTO;
import com.phoenix.common.enumeration.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    @GetMapping("/get")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getUserById(@RequestParam String userId){
        UserVO userVO;
        userVO = userService.getUserById(userId);

        return ResultVO.success(RespMessageConstant.GET_SUCCESS,userVO);
    }

    @GetMapping("/get/cur")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getCurrentUser(){
        UserVO userVO;
        userVO = userService.getUserById(TokenContext.getUserId());

        return ResultVO.success(RespMessageConstant.GET_SUCCESS,userVO);
    }

    @PutMapping("/update")
    @AuthorizationRequired(Role.MEMBER)
    @FilterNeeded
    public ResultVO updateUser(@RequestBody UserDTO userDTO){
        UserVO userVO = userService.updateUser(userDTO,TokenContext.getUserId());
        return ResultVO.success(RespMessageConstant.UPDATE_SUCCESS,userVO);
    }
    @PostMapping("/register")
    @AuthorizationRequired(Role.VISITOR)
    @FilterNeeded
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
