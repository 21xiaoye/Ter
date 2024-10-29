package com.cabin.ter.chat.mapper;

import com.cabin.ter.chat.domain.FriendRoomDomain;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-30 17:02
 */
public interface FriendRoomDomainMapper {
    /**
     * 获取所有朋友房间信息
     *
     * @param roomIdsList   需要获取的房间Id列表
     * @return  朋友房间信息列表
     */
    List<FriendRoomDomain> listByIds(@Param("roomIdsList") List<Long> roomIdsList);

    /**
     * 保存朋友关系
     * @param friendRoomDomain  需要保存的朋友关系实体对象
     */
    @Insert("INSERT INTO ter_room_friend (roomId, userId, targetId, roomName, createTime) " +
            "VALUES (#{roomId}, #{userId}, #{targetId}, #{roomName}, #{createTime})")
    Integer saveFriendship(FriendRoomDomain friendRoomDomain);

    /**
     * 获取双方朋友关系表
     *
     * @param userId    用户Id
     * @param targetId  好友Id
     */
    @Select("SELECT roomId, userId, targetId, roomName, roomStatus " +
            "FROM ter_room_friend " +
            "WHERE userId=#{userId} AND targetId=#{targetId}")
    FriendRoomDomain getFriendship(Long userId, Long targetId);
    /**
     * 操作朋友关系， 拉黑或者恢复朋友关系
     * @param roomId    房间Id
     */
    @Update("UPDATE ter_room_friend SET roomStatus = #{roomStatus} " +
            "WHERE roomId = #{roomId}")
    Integer operateFriendship(Long roomId, Integer roomStatus);
}
