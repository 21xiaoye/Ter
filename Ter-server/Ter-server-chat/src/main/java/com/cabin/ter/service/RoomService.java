package com.cabin.ter.service;

import com.cabin.ter.chat.domain.GroupRoomDomain;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 19:41
 */
public interface RoomService {
    GroupRoomDomain createGroup(Long uid);
}
