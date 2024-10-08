package com.phoenix.base.core.service.data.chain;

import com.phoenix.base.model.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Slf4j
@Component
@RequiredArgsConstructor
public class DataChangeChainHandler implements Handler {
   static ArrayList<Handler> handlers = new ArrayList<>();

   @Override
   public void handle(MessageDTO messageDTO) {
      //如果数据库中记录的状态和前端传递的状态一样，表示用户没有做操作，直接返回
      if (messageDTO.getArticleDataState() == messageDTO.getArticleDataChangedState()) return;

      for (Handler handler : handlers) {
         handler.handle(messageDTO);
      }
   }

   @Override
   public void registerSelf() {}
}
