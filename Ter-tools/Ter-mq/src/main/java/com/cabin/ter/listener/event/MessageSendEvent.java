package com.cabin.ter.listener.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 15:35
 */
@Getter
public class MessageSendEvent extends ApplicationEvent {
    private final Long msgId;
    public MessageSendEvent(Object source, Long msgId) {
        super(source);
        this.msgId = msgId;
    }
}
