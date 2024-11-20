package com.cabin.ter.listener.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserLogOutEvent extends ApplicationEvent {
    private Long userId;
    public UserLogOutEvent(Object source,Long userId) {
        super(source);
        this.userId = userId;
    }
}
