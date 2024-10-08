package com.phoenix.base.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.base.model.entity.ArticleUpvoteRelation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleUpvoteMapper extends BaseMapper<ArticleUpvoteRelation> {
    @Insert("INSERT IGNORE INTO article_upvote(article_upvote_id,article_id, upvote_user_id) VALUES (#{articleUpvoteId},#{articleId}, #{upvoteUserId})")
    void insertIgnore(ArticleUpvoteRelation articleUpvoteRelation);
}
