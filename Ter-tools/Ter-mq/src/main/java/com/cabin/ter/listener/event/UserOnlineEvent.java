package com.cabin.ter.listener.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserOnlineEvent extends ApplicationEvent {
    private final Long userId;
    private final Long onlineTime;

    public UserOnlineEvent(Object source, Long userId, Long onlineTime) {
        super(source);
        this.userId = userId;
        this.onlineTime = onlineTime;
    }
}