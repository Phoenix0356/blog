package com.phoenix.base.core.service;

import com.phoenix.common.dto.CommentDTO;
import com.phoenix.base.model.vo.CommentVO;

import java.util.List;

public interface CommentService {
    CommentVO getCommentById(String commentId);

    List<CommentVO> getCommentArticleList(String articleId) throws InterruptedException;

    CommentVO saveComment(CommentDTO commentDTO) throws InterruptedException;

    CommentVO updateComment(CommentDTO commentDTO);

    void deleteComment(String commentId);
}
