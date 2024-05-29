package com.cabin.ter.listener.event;

import com.cabin.ter.chat.domain.GroupMemberDomain;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 10:40
 */
@Getter
public class GroupMemberEvent extends ApplicationEvent {
    private final List<GroupMemberDomain> memberList;
    private final GroupRoomDomain roomGroup;
    private final Long inviteUid;
    public GroupMemberEvent(Object source, GroupRoomDomain roomGroup, List<GroupMemberDomain> memberList, Long inviteUid) {
        super(source);
        this.memberList = memberList;
        this.roomGroup = roomGroup;
        this.inviteUid = inviteUid;
    }
}
