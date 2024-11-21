package com.cabin.ter.listener.event;

import com.cabin.ter.constants.response.ChatMessageResp;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 15:35
 */
@Getter
public class MessageSendEvent extends ApplicationEvent {
    private final ChatMessageResp chatMessageResp;
    public MessageSendEvent(Object source, ChatMessageResp chatMessageResp) {
        super(source);
        this.chatMessageResp = chatMessageResp;
    }
}
