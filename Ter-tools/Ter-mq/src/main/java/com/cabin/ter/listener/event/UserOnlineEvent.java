package com.cabin.ter.listener.event;

import com.cabin.ter.vo.UserPrincipal;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserOnlineEvent extends ApplicationEvent {
    private final UserPrincipal userPrincipal;

    public UserOnlineEvent(Object source, UserPrincipal userPrincipal) {
        super(source);
        this.userPrincipal = userPrincipal;
    }
}