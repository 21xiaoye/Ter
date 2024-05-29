package com.cabin.ter.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.adapter.ChatAdapter;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.cache.UserInfoCache;
import com.cabin.ter.chat.domain.GroupMemberDomain;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import com.cabin.ter.chat.domain.RoomDomain;
import com.cabin.ter.chat.enums.GroupRoleEnum;
import com.cabin.ter.chat.enums.RoomTypeEnum;
import com.cabin.ter.chat.mapper.GroupMemberDomainMapper;
import com.cabin.ter.chat.mapper.GroupRoomDomainMapper;
import com.cabin.ter.chat.mapper.RoomDomainMapper;
import com.cabin.ter.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 19:42
 */
@Service
@Slf4j
public class RoomServiceImpl implements RoomService {
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private ChatAdapter chatAdapter;
    @Autowired
    private RoomDomainMapper roomDomainMapper;
    @Autowired
    private GroupRoomDomainMapper groupRoomDomainMapper;
    @Autowired
    private GroupMemberDomainMapper groupMemberDomainMapper;
    @Autowired
    private Snowflake snowflake;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GroupRoomDomain createGroup(Long uid) {
        UserDomain userDomain = userInfoCache.get(uid);
        RoomDomain room = createRoom(RoomTypeEnum.GROUP);

        GroupRoomDomain groupRoomDomain = chatAdapter.buildGroupRoom(userDomain, room.getId());
        groupRoomDomainMapper.saveGroupRoom(groupRoomDomain);

        GroupMemberDomain groupMemberDomain = GroupMemberDomain.builder()
                .id(snowflake.nextId())
                .uid(userDomain.getUId())
                .groupId(room.getId())
                .role(GroupRoleEnum.LEADER.getType())
                .createTime(System.currentTimeMillis())
                .build();

        groupMemberDomainMapper.saveGroupMember(groupMemberDomain);
        return groupRoomDomain;
    }


    /**
     * 创建 群聊
     *
     * @param typeEnum 群聊类型
     * @return
     */
    private RoomDomain createRoom(RoomTypeEnum typeEnum) {
        RoomDomain room = chatAdapter.buildRoom(typeEnum);
        roomDomainMapper.saveRoom(room);
        return room;
    }
}
