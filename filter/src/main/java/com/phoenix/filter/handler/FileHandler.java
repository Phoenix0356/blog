package com.phoenix.filter.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileHandler {

    public List<String> parseFile(MultipartFile file) throws Exception {
        List<String> contentList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()
                , StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length()<=1) continue;
                contentList.add(line);
            }
        } catch (Exception e) {
            throw new Exception();
        }
        return contentList;
    }
}
