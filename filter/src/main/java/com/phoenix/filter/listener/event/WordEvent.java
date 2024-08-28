package com.phoenix.filter.listener.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class WordEvent extends ApplicationEvent {

    EventType eventType;
    public WordEvent(EventType eventType) {
        super(eventType);
        this.eventType = eventType;
    }
    public enum EventType{
        UPLOAD_FILE,
        DELETE_LIST,
        UPLOAD_LIST
    }
}
