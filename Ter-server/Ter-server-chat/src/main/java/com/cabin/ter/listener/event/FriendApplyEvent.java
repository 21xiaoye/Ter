package com.cabin.ter.listener.event;

import com.cabin.ter.constants.response.FriendApplyRecordInfoResp;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class FriendApplyEvent extends ApplicationEvent {
    private final Long targetId;
    private final FriendApplyRecordInfoResp friendApplyResp;
    public FriendApplyEvent(Object source, FriendApplyRecordInfoResp friendApplyResp, Long targetId) {
        super(source);
        this.friendApplyResp = friendApplyResp;
        this.targetId = targetId;
    }
}
