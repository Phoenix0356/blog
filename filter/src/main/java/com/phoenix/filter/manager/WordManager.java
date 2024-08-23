package com.phoenix.filter.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phoenix.filter.entity.SensitiveWord;
import com.phoenix.filter.manager.mapper.WordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WordManager {
    private final WordMapper wordMapper;

    public List<String> loadAllWord(){
        return wordMapper.selectList(null)
                .stream().map(SensitiveWord::getWordString)
                .toList();
    }

    public void importList(List<SensitiveWord> list){
        list.forEach(wordMapper::insertIgnore);
    }


}
