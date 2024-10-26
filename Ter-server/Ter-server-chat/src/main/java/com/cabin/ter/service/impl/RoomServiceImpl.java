package com.cabin.ter.service.impl;

import com.cabin.ter.adapter.ChatAdapter;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.cache.UserInfoCache;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import com.cabin.ter.chat.domain.RoomDomain;
import com.cabin.ter.chat.enums.RoomTypeEnum;
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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public GroupRoomDomain createGroup(Long uid) {
        // 获取创建者信息
        UserDomain userDomain = userInfoCache.get(uid);
        // 创建房间基本信息
        RoomDomain room = this.createRoom(RoomTypeEnum.GROUP,uid);
        // 构建群组基本信息
        GroupRoomDomain groupRoomDomain = chatAdapter.buildGroupRoom(userDomain, room.getId());
        // 保存群组
        groupRoomDomainMapper.saveGroupRoom(groupRoomDomain);
        return groupRoomDomain;
    }
    /**
     * 创建群聊
     *
     * @param typeEnum 群聊类型
     * @return
     */
    private RoomDomain createRoom(RoomTypeEnum typeEnum,Long uid) {
        RoomDomain room = chatAdapter.buildRoom(typeEnum, uid);
        roomDomainMapper.saveRoom(room);
        return room;
    }
}
