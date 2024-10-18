package com.phoenix.user.core.controller;

import com.phoenix.common.client.FilterServiceClient;
import com.phoenix.user.context.TokenContext;
import com.phoenix.user.core.service.UserService;
import com.phoenix.common.vo.ResultVO;
import com.phoenix.user.model.vo.UserVO;
import com.phoenix.common.annotation.AuthorizationRequired;
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
    final FilterServiceClient filterServiceClient;

    @GetMapping("/{userId}")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO getUserById(@PathVariable("userId") String userId){
        UserVO userVO = userService.getUserByUserId(userId);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,userVO);
    }

    @GetMapping("/get/cur")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getCurrentUser(){
        UserVO userVO = userService.getCurUser(TokenContext.getUserId());
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,userVO);
    }

    @PutMapping("/update")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO updateUser(@RequestBody UserDTO userDTO){
        //检测是否存在敏感词
        if (filterServiceClient.detectText(userDTO.getUsername())){
            return ResultVO.error(RespMessageConstant.SENSITIVE_WORD_DETECTED);
        }
        UserVO userVO = userService.updateUser(userDTO,TokenContext.getUserId());
        return ResultVO.success(RespMessageConstant.UPDATE_SUCCESS,userVO);
    }
    @PostMapping("/register/{sessionId}")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO register(@RequestBody UserRegisterDTO userRegisterDTO,
                             @PathVariable("sessionId") String sessionId){
        //检测是否存在敏感词,存在则重新输入
        if (filterServiceClient.detectText(userRegisterDTO.getUsername())){
            return ResultVO.error(RespMessageConstant.SENSITIVE_WORD_DETECTED);
        }
        UserVO userVO = userService.register(userRegisterDTO,sessionId);
        return ResultVO.success(RespMessageConstant.REGISTER_SUCCESS,userVO);
    }
    @PostMapping("/login/{sessionId}")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO login(@RequestBody UserLoginDTO userLoginDTO,
                          @PathVariable("sessionId") String sessionId){
        UserVO userVO = userService.login(userLoginDTO,sessionId);
        return ResultVO.success(RespMessageConstant.LOGIN_SUCCESS,userVO);
    }

    @PostMapping("/logout/{sessionId}")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO logout(@PathVariable("sessionId") String sessionId){
        userService.logout(sessionId);
        return ResultVO.success(RespMessageConstant.LOGOUT_SUCCESS);
    }
}
