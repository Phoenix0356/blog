package com.phoenix.blog.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.config.JwtConfig;
import com.phoenix.blog.config.PictureConfig;
import com.phoenix.blog.config.URLConfig;
import com.phoenix.blog.constant.HttpConstant;
import com.phoenix.blog.constant.RespMessageConstant;
import com.phoenix.blog.core.manager.UserManager;
import com.phoenix.blog.core.service.UserLogService;
import com.phoenix.blog.exceptions.clientException.*;
import com.phoenix.blog.model.dto.UserDTO;
import com.phoenix.blog.model.dto.UserLoginDTO;
import com.phoenix.blog.model.dto.UserRegisterDTO;
import com.phoenix.blog.model.entity.User;
import com.phoenix.blog.core.mapper.UserMapper;
import com.phoenix.blog.core.service.UserService;
import com.phoenix.blog.model.vo.UserVO;
import com.phoenix.blog.util.DataUtil;
import com.phoenix.blog.util.JwtUtil;
import com.phoenix.blog.util.PictureUtil;
import com.phoenix.blog.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final UserLogService userLogService;
    final UserMapper userMapper;
    final UserManager userManager;

    final URLConfig urlConfig;
    final PictureConfig pictureConfig;
    final JwtConfig jwtConfig;

    @Override
    public UserVO getUserById(String userId) {
        if (DataUtil.isEmptyData(userId)) throw new InvalidateArgumentException();

        User user = userManager.selectUserInCache(userId);
        if (user == null) {
            throw new NotFoundException(RespMessageConstant.USER_NOT_FOUND_ERROR);
        }
        return UserVO.BuildVO(user,null);
    }

    @Override
    public UserVO getUserByUsername(String username){
        if (DataUtil.isEmptyData(username)) throw new InvalidateArgumentException();

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username",username));

        if (user == null){
            throw new NotFoundException(RespMessageConstant.USER_NOT_FOUND_ERROR);
        }
        return UserVO.BuildVO(user,null);
    }

    @Override
    public UserVO updateUser(UserDTO userDTO,String userId) {
        if (DataUtil.isEmptyData(userId)) throw new InvalidateArgumentException();

        User user = userMapper.selectById(userId);

        if (user == null) throw new NotFoundException(RespMessageConstant.USER_NOT_FOUND_ERROR);

        String newUsername = userDTO.getUsername();

        user.setUsername(newUsername);

        if (userMapper.selectOne(new QueryWrapper<User>().eq("username",newUsername))!=null){
            throw new AlreadyExistsException(RespMessageConstant.USERNAME_ALREADY_EXISTS_ERROR);
        }
        userMapper.updateById(user);

        return UserVO.BuildVO(user,null);
    }

    @Override
    public UserVO register(UserRegisterDTO userRegisterDTO) {
        BCryptPasswordEncoder passwordEncoder = SecurityUtil.getPasswordEncoder();
        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();
        String avatarName = PictureUtil.saveOrUpdateFile(userRegisterDTO.getAvatarBase64(), null,pictureConfig.defaultAvatarPath,true);

        String avatarURL = HttpConstant.HTTPS_PREFIX+urlConfig.dnsName
                +pictureConfig.getDefaultAvatarURL()
                +avatarName;

        if (userMapper.selectOne(new QueryWrapper<User>().eq("username",username))!=null){
            throw new AlreadyExistsException(RespMessageConstant.USERNAME_ALREADY_EXISTS_ERROR);
        }

        User user = new User();

        user.setUsername(username)
        .setPassword(passwordEncoder.encode(password))
        .setUserRole(userRegisterDTO.getRole())
        .setUserAvatarURL(avatarURL)
        .setRegisterTime(new Timestamp(System.currentTimeMillis()));

        userMapper.insert(user);
        String token = JwtUtil.getJwt(user.getUserId(), user.getUserRole().name(),
                jwtConfig.secret, jwtConfig.expiration);
        userManager.setIntoCache(user.getUserId(),"",jwtConfig.expiration);

        //记录日志
        userLogService.saveUserLog(user);
        return UserVO.BuildVO(user,token);
    }

    @Override
    public UserVO login(UserLoginDTO userLoginDTO) {

        BCryptPasswordEncoder passwordEncoder = SecurityUtil.getPasswordEncoder();

        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
        if (user == null){
            throw new NotFoundException(RespMessageConstant.USER_NOT_FOUND_ERROR);
        }

        if (!passwordEncoder.matches(password,user.getPassword())){
            throw new UsernameOrPasswordErrorException(RespMessageConstant.USERNAME_OR_PASSWORD_ERROR);
        }

        String token = JwtUtil.getJwt(user.getUserId(), user.getUserRole().name(),
                jwtConfig.secret, jwtConfig.expiration);
        userManager.setIntoCache(user.getUserId(),"",jwtConfig.expiration);

        //记录日志
        userLogService.saveUserLog(user);

        return UserVO.BuildVO(user,token);
    }

    @Override
    public void logout(String jwtId, String userId, Date jwtExpirationTime) {
        long expTime = Math.max(jwtExpirationTime.getTime()-System.currentTimeMillis(),0);
        userManager.setIntoCache(jwtId,"",expTime/1000);
        userManager.deleteCache(userId);
    }
}
