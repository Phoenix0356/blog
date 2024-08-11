package com.phoenix.filter.service;

import com.phoenix.filter.filter.TextFilter;
import com.phoenix.filter.manager.WordManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilterService {
    final TextFilter textFilter;

    public String filterSensitiveWord(String text){
        return textFilter.filterText(text);
    }
}
