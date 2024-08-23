package com.phoenix.filter.service;

import com.phoenix.filter.entity.SensitiveWord;
import com.phoenix.filter.filter.matcher.WordMatcher;
import com.phoenix.filter.handler.FileHandler;
import com.phoenix.filter.init.WordMatcherInitializer;
import com.phoenix.filter.manager.WordManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordService {
    final WordManager wordManager;
    final FileHandler fileHandler;
    final WordMatcher wordMatcher;

    public void uploadSensitiveWordFromFiles(MultipartFile file){
        List<String> contentList = fileHandler.parseFile(file);
        wordMatcher.addWord(contentList);

        List<SensitiveWord> sensitiveWordList = contentList
                .stream().map(s -> new SensitiveWord().setWordString(s))
                .toList();
        wordManager.importList(sensitiveWordList);
    }

    public void saveSensitiveWord(List<String> sensitiveWordList){

    }

}
