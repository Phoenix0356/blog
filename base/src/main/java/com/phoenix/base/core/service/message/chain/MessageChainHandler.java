package com.phoenix.base.core.service.message.chain;

import com.phoenix.base.model.dto.MessageDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageChainHandler implements Handler {
   static ArrayList<Handler> handlers = new ArrayList<>();

   @Override
   public void handle(MessageDTO messageDTO) {
      for (Handler handler : handlers) {
         handler.handle(messageDTO);
      }
   }

   @Override
   public void registerSelf() {}
}
