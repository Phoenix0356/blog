package com.phoenix.base.core.service.data.chain;

import com.phoenix.base.model.dto.MessageDTO;
import org.springframework.stereotype.Component;

@Component
public interface Handler{
    void handle(MessageDTO messageDTO);
    void registerSelf();
}
