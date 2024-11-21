package com.cabin.ter.listener.event;

import com.cabin.ter.chat.domain.GroupMemberDomain;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-31 19:55
 */
@Getter
public class GroupMemberAddEvent extends ApplicationEvent {
    private final List<GroupMemberDomain> memberDomainList;
    private final GroupRoomDomain groupRoomDomain;
    private final Long inviteUid;
    public GroupMemberAddEvent(Object source, GroupRoomDomain groupMemberDomain, List<GroupMemberDomain> memberDomainList , Long inviteUid) {
        super(source);
        this.memberDomainList = memberDomainList;
        this.groupRoomDomain = groupMemberDomain;
        this.inviteUid = inviteUid;
    }
}
