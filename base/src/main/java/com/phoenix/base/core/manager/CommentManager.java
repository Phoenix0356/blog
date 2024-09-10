package com.phoenix.base.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.base.core.mapper.CommentMapper;
import com.phoenix.base.model.entity.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
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
            log.error("删除ID为 {} 的文章的评论失败",articleId);
        }
    }
    public void delete(String commentId){
        int result = commentMapper.deleteById(commentId);
        if (result == 0){
            log.error("删除ID为 {} 的评论行失败",commentId);
        }
    }
}
