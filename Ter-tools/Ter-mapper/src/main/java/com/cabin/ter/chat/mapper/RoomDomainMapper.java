package com.cabin.ter.chat.mapper;

import com.cabin.ter.chat.domain.RoomDomain;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 20:24
 */
public interface RoomDomainMapper {
    /**
     * 插入群
     *
     * @param roomDomain
     * @return
     */
    @Insert("INSERT INTO ter_room(roomId, userId, roomType, hotFlag, createTime) VALUES(#{roomId}, #{userId}, #{roomType}, #{hotFlag}, #{createTime})")
    Integer saveRoom(RoomDomain roomDomain);

    /**
     * 根据roomId列表获取群列表
     *
     * @param roomIdsList
     * @return
     */
    List<RoomDomain> listByIds(@Param("roomIdsList") List<Long> roomIdsList);
}
