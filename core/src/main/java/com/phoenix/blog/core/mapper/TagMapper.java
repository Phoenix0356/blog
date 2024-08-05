package com.phoenix.blog.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.model.entity.Tag;
import com.phoenix.blog.model.vo.TagVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    List<TagVO> getArticleTagList(String articleId);
    void addTagToArticle(Map<String,String> map);
}
