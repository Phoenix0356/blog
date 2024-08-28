package com.phoenix.filter.service;

import com.phoenix.filter.filter.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilterService {
    final Filter filter;

    public String filterText(String text){
        return filter.filterText(text);
    }

    public Boolean detectText(String text){
        return filter.detectText(text);
    }

//    public Object filterObject(Object object){
//        return filter.filterObjectText(object);
//    }
}
