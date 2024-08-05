package com.phoenix.blog.core.controller;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.constant.RespMessageConstant;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.service.MessageService;
import com.phoenix.blog.enumeration.Role;
import com.phoenix.blog.model.vo.ArticleMessageVO;
import com.phoenix.blog.model.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    final MessageService messageService;

    @GetMapping("/article/{messageId}")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getMessageByMessageId(@PathVariable String messageId){
        ArticleMessageVO articleMessageVO = messageService.getMessageByMessageId(messageId);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,articleMessageVO);
    }

    @GetMapping("/article")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO getMessageListByReceiverId(){
        List<ArticleMessageVO> articleMessageVOList = messageService.getMessageListByReceiverId(TokenContext.getUserId());
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,articleMessageVOList);
    }

    @PutMapping("/confirm/{messageType}")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO confirmMessages(@PathVariable int messageType){
        messageService.confirmMessage(TokenContext.getUserId(),messageType);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS);
    }
}
