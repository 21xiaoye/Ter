package com.cabin.ter.service;

import com.cabin.ter.chat.domain.FriendRoomDomain;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import com.cabin.ter.chat.domain.RoomDomain;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 19:41
 */
public interface RoomService {
    /**
     * 创建群聊房间
     *
     * @param uid   创建者uId
     * @return  返回群聊房间实体对象
     */
    GroupRoomDomain createGroup(Long uid);

    /**
     * 创建单聊房间
     *
     * @param userId    申请者uId
     * @param targetId  被申请者uId
     */
    FriendRoomDomain createFriend(Long userId, Long targetId);
}
