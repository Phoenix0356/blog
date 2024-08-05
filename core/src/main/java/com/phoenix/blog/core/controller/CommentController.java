package com.phoenix.blog.core.controller;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.constant.RespMessageConstant;
import com.phoenix.blog.context.TokenContext;
import com.phoenix.blog.core.service.CommentService;
import com.phoenix.blog.enumeration.Role;
import com.phoenix.blog.model.dto.CommentDTO;
import com.phoenix.blog.model.vo.CommentVO;
import com.phoenix.blog.model.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/get/{commentId}")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO getCommentById(@PathVariable String commentId){
        CommentVO commentVO = commentService.getCommentById(commentId);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS, commentVO);
    }

    @GetMapping("/get/all")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO getArticleCommentAll(@RequestParam String articleId) throws InterruptedException {
        List<CommentVO> commentVOList = commentService.getCommentArticleList(articleId);
        return ResultVO.success(RespMessageConstant.GET_SUCCESS,commentVOList);
    }

    @PostMapping("/save")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO  saveCommentById(@RequestBody CommentDTO commentDTO) throws InterruptedException {
        commentDTO.setCommentUserId(TokenContext.getUserId());
        CommentVO commentVO = commentService.saveComment(commentDTO);
        return ResultVO.success(RespMessageConstant.SAVE_SUCCESS, commentVO);
    }

    @PutMapping("/update")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO updateCommentById(@RequestBody CommentDTO commentDTO){
        CommentVO commentVO = commentService.updateComment(commentDTO);
        return ResultVO.success(RespMessageConstant.UPDATE_SUCCESS,commentVO);
    }

    @DeleteMapping("/delete/{commentId}")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO deleteComment(@PathVariable String commentId){
        commentService.deleteComment(commentId);
        return ResultVO.success(RespMessageConstant.DELETE_SUCCESS,null);
    }
}
