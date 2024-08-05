package com.phoenix.blog.core.controller;

import com.phoenix.blog.annotations.AuthorizationRequired;
import com.phoenix.blog.core.service.ReplyService;
import com.phoenix.blog.enumeration.Role;
import com.phoenix.blog.model.dto.ReplyDTO;
import com.phoenix.blog.model.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    final private ReplyService replyService;

    @GetMapping("/get")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO getReply(@RequestParam String replyId){
        return null;
    }

    @GetMapping("/get/{commentId}")
    @AuthorizationRequired(Role.VISITOR)
    public ResultVO getCommentReplyList(@PathVariable String commentId){
        return null;
    }

    @PostMapping("/save")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO saveReply(@RequestBody ReplyDTO replyDTO){
        return null;
    }

    @PutMapping("/update")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO updateReply(@RequestBody ReplyDTO replyDTO){
        return null;
    }

    @DeleteMapping("/delete")
    @AuthorizationRequired(Role.MEMBER)
    public ResultVO deleteReply(@RequestParam String replyId){
        return null;
    }

}
