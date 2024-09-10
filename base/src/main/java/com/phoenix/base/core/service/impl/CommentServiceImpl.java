package com.phoenix.base.core.service.impl;

import com.phoenix.base.core.manager.CommentManager;
import com.phoenix.base.core.mapper.CommentMapper;
import com.phoenix.base.core.service.CommentService;
import com.phoenix.common.exceptions.clientException.CommentFormatException;
import com.phoenix.common.exceptions.clientException.CommentNotFoundException;
import com.phoenix.common.exceptions.clientException.InvalidateArgumentException;
import com.phoenix.common.dto.CommentDTO;
import com.phoenix.base.model.entity.Comment;
import com.phoenix.base.model.vo.CommentVO;
import com.phoenix.common.util.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentMapper commentMapper;
    private final CommentManager commentManager;

    @Override
    public CommentVO getCommentById(String commentId) {
        if (DataUtil.isEmptyData(commentId)){
            throw  new InvalidateArgumentException();
        }

        Comment comment= commentMapper.selectById(commentId);

        if (comment == null){
            throw new CommentNotFoundException();
        }

        return CommentVO.buildVO(comment);
    }

    @Override
    public List<CommentVO> getCommentArticleList(String articleId){
        if (DataUtil.isEmptyData(articleId)) throw new InvalidateArgumentException();
        return commentManager.selectListByArticleId(articleId)
                .stream().map(CommentVO::buildVO)
                .toList();
    }

    @Override
    public CommentVO saveComment(CommentDTO commentDTO){
        if (commentDTO.getCommentContent() == null){
            throw new CommentFormatException();
        }
        Comment comment = new Comment();

        comment.setCommentContent(commentDTO.getCommentContent())
                .setCommentArticleId(commentDTO.getCommentArticleId())
                .setCommentUserId(commentDTO.getCommentUserId())
                .setCommentReviseTime(new Timestamp(System.currentTimeMillis()));

        commentMapper.insert(comment);
        return CommentVO.buildVO(comment);
    }

    @Override
    public CommentVO updateComment(CommentDTO commentDTO) {
        String commentId = commentDTO.getCommentId();
        if (DataUtil.isEmptyData(commentId)) throw new InvalidateArgumentException();

        Comment comment = commentMapper.selectById(commentId);

        if (comment == null) throw new CommentNotFoundException();
        comment.setCommentContent(commentDTO.getCommentContent())
                .setCommentReviseTime(new Timestamp(System.currentTimeMillis()));
        commentMapper.updateById(comment);
        return CommentVO.buildVO(comment);
    }

    @Override
    public void deleteComment(String commentId) {
        if (DataUtil.isEmptyData(commentId)) throw new InvalidateArgumentException();

        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) throw new CommentNotFoundException();

        commentMapper.deleteById(commentId);
    }
}
