package com.cabin.ter.listener.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserOfflineEvent extends ApplicationEvent {
    private final Long userId;
    private final Long offlineTime;
    public UserOfflineEvent(Object source, Long userId, Long offlineTime) {
        super(source);
        this.userId = userId;
        this.offlineTime = offlineTime;
    }
}
