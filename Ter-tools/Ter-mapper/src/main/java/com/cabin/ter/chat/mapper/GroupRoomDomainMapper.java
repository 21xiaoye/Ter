package com.cabin.ter.chat.mapper;

import com.cabin.ter.chat.domain.GroupMemberDomain;
import com.cabin.ter.chat.domain.GroupRoomDomain;
import com.cabin.ter.chat.domain.RoomDomain;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-28 20:25
 */
public interface GroupRoomDomainMapper {

    @Insert("INSERT INTO ter_room_group (roomId, roomName, roomAvatar, createTime) values (#{groupRoomDomain.roomId},#{groupRoomDomain.roomName},#{groupRoomDomain.roomAvatar},#{groupRoomDomain.createTime})")
    Integer saveGroupRoom(@Param("groupRoomDomain") GroupRoomDomain groupRoomDomain);

    List<GroupRoomDomain> listByIds(@Param("roomIdsList") List<Long> roomIdsList);

    @Select("SELECT roomId, roomName, roomAvatar, deleteStatus FROM ter_room_group WHERE roomId = #{roomId}")
    GroupRoomDomain getByRoomId(@Param("roomId") Long roomId);
}
