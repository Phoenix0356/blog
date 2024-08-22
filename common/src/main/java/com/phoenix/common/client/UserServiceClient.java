package com.phoenix.common.client;

import com.phoenix.common.dto.UserDTO;
import com.phoenix.common.dto.UserLoginDTO;
import com.phoenix.common.dto.UserRegisterDTO;
import com.phoenix.common.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user")
public interface UserServiceClient {

    @GetMapping("/get/cur")
    ResultVO getCurrentUser();

    @GetMapping("/get")
    ResultVO getUserById(@RequestBody String userId);

    @PutMapping("/update")
    ResultVO updateUser(@RequestBody UserDTO userDTO);

    @PostMapping("/register")
    ResultVO register(@RequestBody UserRegisterDTO userRegisterDTO);

    @PostMapping("/login")
    ResultVO login(@RequestBody UserLoginDTO userLoginDTO);

    @PostMapping("/logout")
    ResultVO logout();
}

