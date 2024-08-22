package com.phoenix.base.core.controller;

import com.phoenix.common.annotation.AuthorizationRequired;
import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.base.context.TokenContext;
import com.phoenix.base.core.service.MessageService;
import com.phoenix.common.enumeration.Role;
import com.phoenix.base.model.vo.ArticleMessageVO;
import com.phoenix.common.vo.ResultVO;
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
