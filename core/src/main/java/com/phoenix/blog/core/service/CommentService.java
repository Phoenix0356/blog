package com.phoenix.blog.core.service;

import com.phoenix.blog.model.dto.CommentDTO;
import com.phoenix.blog.model.vo.CommentVO;

import java.util.List;

public interface CommentService {
    CommentVO getCommentById(String commentId);

    List<CommentVO> getCommentArticleList(String articleId) throws InterruptedException;

    CommentVO saveComment(CommentDTO commentDTO) throws InterruptedException;

    CommentVO updateComment(CommentDTO commentDTO);

    void deleteComment(String commentId);
}
