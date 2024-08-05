package com.phoenix.blog.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.blog.core.mapper.CommentMapper;
import com.phoenix.blog.model.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentManager {
    private final CommentMapper commentMapper;

    public Comment select(String commentId){
        return commentMapper.selectById(commentId);
    }

    public List<Comment> selectListByArticleId(String articleId){
        return commentMapper.selectList(new QueryWrapper<Comment>()
                .eq("comment_article_id",articleId));
    }

    public void deleteByArticleId(String articleId){
        int result = commentMapper.delete(new QueryWrapper<Comment>()
                .eq("comment_article_id",articleId));
        if (result == 0){
            //todo 记录日志
        }
    }
    public void delete(String commentId){
        int result = commentMapper.deleteById(commentId);
        if (result == 0){
            //todo 记录日志
        }
    }
}
