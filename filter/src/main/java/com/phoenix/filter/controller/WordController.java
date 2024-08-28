package com.phoenix.filter.controller;

import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.common.vo.ResultVO;
import com.phoenix.filter.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@RestController
@RequiredArgsConstructor
public class WordController {
    final WordService wordService;
    @PostMapping("/upload")
    public ResultVO uploadFile(@RequestParam("file") MultipartFile file){
        CompletableFuture<Boolean> completableFuture = wordService.asyncUploadWordFromFiles(file);
        boolean isUploadSuccess;
        try {
            isUploadSuccess = completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return isUploadSuccess?ResultVO.success(RespMessageConstant.SAVE_SUCCESS):
                ResultVO.success(RespMessageConstant.SAVE_ERROR);
    }
}
