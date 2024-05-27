package com.cabin.ter.listener.event;

import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.vo.UserPrincipal;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserOfflineEvent extends ApplicationEvent {
    public final UserDomain userDomain;
    public UserOfflineEvent(Object source, UserDomain userDomain) {
        super(source);
        this.userDomain = userDomain;
    }
}
