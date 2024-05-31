package com.cabin.ter.listener;

import com.cabin.ter.adapter.MemberAdapter;
import com.cabin.ter.adapter.RoomAdapter;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.cache.GroupMemberCache;
import com.cabin.ter.cache.UserInfoCache;
import com.cabin.ter.chat.domain.GroupMemberDomain;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import com.cabin.ter.config.ThreadPoolConfig;
import com.cabin.ter.constants.vo.response.WSBaseResp;
import com.cabin.ter.listener.event.GroupMemberAddEvent;
import com.cabin.ter.service.ChatService;
import com.cabin.ter.service.PushService;
import com.cabin.ter.vo.request.ChatMessageReq;
import com.cabin.ter.vo.response.WSMemberChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xiaoye
 * @date Created in 2024-05-31 20:02
 */
@Slf4j
@Component
public class GroupMemberAddListener {
    @Autowired
    private ChatService chatService;
    @Autowired
    private PushService pushService;
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private GroupMemberCache groupMemberCache;


    @Async(value = ThreadPoolConfig.WS_EXECUTOR)
    @TransactionalEventListener(classes = GroupMemberAddEvent.class, fallbackExecution = true)
    public void sendAddMsg(GroupMemberAddEvent event){
        List<GroupMemberDomain> memberDomainList = event.getMemberDomainList();
        GroupRoomDomain groupRoomDomain = event.getGroupRoomDomain();

        Long inviteUid = event.getInviteUid();
        UserDomain userDomain = userInfoCache.get(inviteUid);

        List<Long> uidList = memberDomainList.stream().map(GroupMemberDomain::getUid).collect(Collectors.toList());
        ChatMessageReq chatMessageReq = RoomAdapter.buildGroupAddMessage(groupRoomDomain, userDomain, userInfoCache.getBatch(uidList));
        chatService.senMsg(chatMessageReq, UserDomain.UID_SYSTEM);
    }

    @Async(value = ThreadPoolConfig.WS_EXECUTOR)
    @TransactionalEventListener(classes = GroupMemberAddEvent.class, fallbackExecution = true)
    public void sendChangePush(GroupMemberAddEvent event){
        List<GroupMemberDomain> memberDomainList = event.getMemberDomainList();
        GroupRoomDomain groupRoomDomain = event.getGroupRoomDomain();

        List<Long> memberUidList = groupMemberCache.getMemberUidList(groupRoomDomain.getRoomId());
        List<Long> uidList = memberDomainList.stream().map(GroupMemberDomain::getUid).collect(Collectors.toList());
        Map<Long, UserDomain> userDomainMap = userInfoCache.getBatch(uidList);

        List<UserDomain> userDomainList = userDomainMap.values().stream().collect(Collectors.toList());

        userDomainList.forEach(user -> {
            WSBaseResp<WSMemberChange> ws = MemberAdapter.buildMemberAddWS(groupRoomDomain.getRoomId(), user);
            pushService.sendPushMsg(ws, memberUidList);
        });
        //移除缓存
        groupMemberCache.evictMemberUidList(groupRoomDomain.getRoomId());
    }

}
