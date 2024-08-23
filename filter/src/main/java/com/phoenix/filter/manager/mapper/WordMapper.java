package com.phoenix.filter.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.filter.entity.SensitiveWord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WordMapper extends BaseMapper<SensitiveWord> {
    @Insert("INSERT IGNORE INTO word (word_id, word_string) VALUES (#{wordId}, #{wordString})")
    void insertIgnore(SensitiveWord word);
}
