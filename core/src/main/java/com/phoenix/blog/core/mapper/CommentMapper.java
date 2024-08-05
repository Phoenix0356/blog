package com.phoenix.blog.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.model.entity.Comment;
import com.phoenix.blog.model.vo.ArticleVO;
import com.phoenix.blog.model.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    CommentVO selectCommentWithPublisher(String commentId);
    List<CommentVO> selectCommentWithPublisherList(String articleId);
}
