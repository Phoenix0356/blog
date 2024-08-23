package com.phoenix.filter.controller;

import com.phoenix.common.constant.RespMessageConstant;
import com.phoenix.common.vo.ResultVO;
import com.phoenix.filter.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
public class WordController {
    final WordService wordService;

    @PostMapping("/upload")
    public ResultVO uploadFile(@RequestParam("file") MultipartFile file){
        wordService.uploadSensitiveWordFromFiles(file);
        return ResultVO.success(RespMessageConstant.SAVE_SUCCESS);
    }
}