package com.phoenix.filter.service;

import com.phoenix.filter.manager.WordManager;
import com.phoenix.filter.manager.mapper.WordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensitiveWordService {
    final WordManager wordManager;

    public void saveSensitiveWord(List<String> sensitiveWordList){

    }

}
