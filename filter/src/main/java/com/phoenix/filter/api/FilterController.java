package com.phoenix.filter.api;

import com.phoenix.filter.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {
    final FilterService filterService;

    @PostMapping("/text")
    public String filterSensitiveWord(@RequestBody String inputText){
        return filterService.filterSensitiveWord(inputText);
    }

}
