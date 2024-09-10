package com.phoenix.filter.controller;

import com.phoenix.filter.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FilterController {
    final FilterService filterService;
    @PostMapping("/text")
    public String filterText(@RequestBody String inputText){
//        System.out.println(inputText+" "+filterService.filterText(inputText));
        return filterService.filterText(inputText);
    }

    @PostMapping("/detect")
    public Boolean detectText(@RequestBody String inputText){
        return filterService.detectText(inputText);
    }

//    @PostMapping("/content")
//    public Object filterSensitiveWord(@RequestBody ClientRequest clientRequest){
//        Object object = clientRequest.getObject();
//        Class<?> clazz = clientRequest.getClazz();
//        clazz.cast(object);
//        System.out.println("我被调用了"+clientRequest.toString());
//        System.out.println(filterService.filterObject(object).toString());
//        return filterService.filterObject(object);
//    }



}
