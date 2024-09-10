package com.phoenix.base.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.base.model.entity.Article;
import com.phoenix.base.model.entity.ArticleUpvote;
import com.phoenix.base.model.vo.ArticleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleUpvoteMapper extends BaseMapper<ArticleUpvote> {
}
