package com.phoenix.filter.service;

import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.filter.entity.SensitiveWord;
import com.phoenix.filter.excpetion.FileUploadException;
import com.phoenix.filter.handler.FileHandler;
import com.phoenix.filter.listener.event.WordEvent;
import com.phoenix.filter.manager.WordManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordManager wordManager;
    private final FileHandler fileHandler;
    private final ApplicationEventPublisher publisher;
    @Async("asyncServiceExecutor")
    public CompletableFuture<Boolean> asyncUploadWordFromFiles(MultipartFile file){
        List<String> contentList;
        try {
            contentList = fileHandler.parseFile(file);
            List<SensitiveWord> sensitiveWordList = contentList
                    .stream().map(s -> new SensitiveWord().setWordString(s))
                    .toList();
            wordManager.importList(sensitiveWordList);
        } catch (Exception e) {
            throw new FileUploadException(RespMessageConstant.FILE_UPLOAD_ERROR);
        }
        publisher.publishEvent(new WordEvent(WordEvent.EventType.UPLOAD_FILE));
        return CompletableFuture.completedFuture(true);
    }
}
