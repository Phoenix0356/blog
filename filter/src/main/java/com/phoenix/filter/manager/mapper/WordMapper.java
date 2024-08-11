package com.phoenix.filter.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.filter.entity.SensitiveWord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WordMapper extends BaseMapper<SensitiveWord> {
}
