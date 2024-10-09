package com.phoenix.base.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.base.model.entity.ArticleCollectionRelation;
import com.phoenix.base.model.entity.Collection;
import com.phoenix.base.model.vo.ArticleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleCollectionMapper extends BaseMapper<ArticleCollectionRelation> {

}
