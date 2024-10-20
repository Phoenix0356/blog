package com.phoenix.user.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.user.config.JwtConfig;
import com.phoenix.user.config.PictureConfig;
import com.phoenix.user.config.URLConfig;
import com.phoenix.user.context.TokenContext;
import com.phoenix.user.core.manager.UserManager;
import com.phoenix.user.core.mapper.UserMapper;
import com.phoenix.user.core.service.UserLogService;
import com.phoenix.user.core.service.UserService;
import com.phoenix.user.enumeration.UserOperation;
import com.phoenix.user.model.entity.User;
import com.phoenix.user.model.vo.UserVO;
import com.phoenix.common.util.DataUtil;
import com.phoenix.common.util.PictureUtil;
import com.phoenix.common.util.SecurityUtil;
import com.phoenix.common.constant.HttpConstant;
import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.common.dto.UserDTO;
import com.phoenix.common.dto.UserLoginDTO;
import com.phoenix.common.dto.UserRegisterDTO;
import com.phoenix.common.exceptions.clientException.AlreadyExistsException;
import com.phoenix.common.exceptions.clientException.InvalidateArgumentException;
import com.phoenix.common.exceptions.clientException.NotFoundException;
import com.phoenix.common.exceptions.clientException.UsernameOrPasswordErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserLogService userLogService;
    private final SessionServiceImpl sessionService;
    private final UserMapper userMapper;
    private final UserManager userManager;

    private final URLConfig urlConfig;
    private final PictureConfig pictureConfig;
    private final JwtConfig jwtConfig;

    @Override
    public UserVO getCurUser(String userId) {
        if (DataUtil.isEmptyData(userId)) throw new InvalidateArgumentException();

        User user = userManager.selectByUserIdInCache(userId);
        if (user == null) {
            throw new NotFoundException(RespMessageConstant.USER_NOT_FOUND_ERROR);
        }
        return UserVO.BuildVO(user);
    }

    @Override
    public UserVO getUserByUserId(String userId){
        if (DataUtil.isEmptyData(userId)) throw new InvalidateArgumentException();

        User user = userManager.selectByUserIdInCache(userId);
        if (user == null){
            throw new NotFoundException(RespMessageConstant.USER_NOT_FOUND_ERROR);
        }
        return UserVO.BuildVO(user);
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
        userManager.deleteCache(userId);

        return UserVO.BuildVO(user);
    }

    @Override
    public UserVO register(UserRegisterDTO userRegisterDTO,String sessionId) {
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
        userManager.setIntoCache(user.getUserId(),"");
        //更新session
        sessionService.upgradeSession(user.getUserId(),sessionId);
        //记录日志
        userLogService.saveUserLog(user,UserOperation.REGISTER.name());
        return UserVO.BuildVO(user);
    }

    @Override
    public UserVO login(UserLoginDTO userLoginDTO,String sessionId) {

        BCryptPasswordEncoder passwordEncoder = SecurityUtil.getPasswordEncoder();

        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
        if (user == null){
            throw new NotFoundException(RespMessageConstant.USERNAME_OR_PASSWORD_ERROR);
        }

        if (!passwordEncoder.matches(password,user.getPassword())){
            throw new UsernameOrPasswordErrorException(RespMessageConstant.USERNAME_OR_PASSWORD_ERROR);
        }

        //更新session
        sessionService.upgradeSession(user.getUserId(),sessionId);
        //记录日志
        userLogService.saveUserLog(user,UserOperation.LOGIN.name());

        return UserVO.BuildVO(user);
    }

    @Override
    public void logout(String sessionId) {
        sessionService.deleteSession(sessionId);
        userManager.deleteCache(TokenContext.getUserId());
    }
}
