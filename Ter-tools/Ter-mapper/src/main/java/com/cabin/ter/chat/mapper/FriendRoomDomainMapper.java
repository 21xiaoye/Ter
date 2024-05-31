package com.cabin.ter.chat.mapper;

import com.cabin.ter.chat.domain.FriendRoomDomain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-30 17:02
 */
public interface FriendRoomDomainMapper {
    List<FriendRoomDomain> listByIds(@Param("roomIdsList") List<Long> roomIdsList);
}
