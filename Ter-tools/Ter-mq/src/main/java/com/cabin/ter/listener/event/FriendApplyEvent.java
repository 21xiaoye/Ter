package com.cabin.ter.listener.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class FriendApplyEvent extends ApplicationEvent {
    private final Long userId;
    private final Long targetId;
    public FriendApplyEvent(Object source, Long userId, Long targetId) {
        super(source);
        this.userId = userId;
        this.targetId = targetId;
    }
}
