package com.phoenix.base.core.controller;

import com.phoenix.common.annotation.AuthorizationRequired;
import com.phoenix.base.core.service.ReplyService;
import com.phoenix.common.enumeration.Role;
import com.phoenix.common.dto.ReplyDTO;
import com.phoenix.common.vo.ResultVO;
import lombok.RequiredArgsConstructor;
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
