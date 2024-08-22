package com.phoenix.common.client;

import com.phoenix.common.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "filter",configuration = FeignRequestInterceptor.class)
public interface FilterServiceClient {
    @PostMapping(value = "/filter/text")
    ResultVO filterText(@RequestBody String inputText);

//    @PostMapping("/content")
//    Object filterObject(@RequestBody ClientRequest clientRequest);


}
