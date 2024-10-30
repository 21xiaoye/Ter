package com.cabin.ter.chat.mapper;

import com.cabin.ter.chat.domain.FriendRoomDomain;
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
    Integer saveFriendship(List<FriendRoomDomain> friendRoomDomain);

    /**
     * 获取双方朋友关系信息
     *
     * @param userId    用户Id
     * @param friendId  好友Id
     */
    @Select("SELECT roomId, userId, friendId, roomName, roomStatus " +
            "FROM ter_room_friend " +
            "WHERE userId=#{userId} AND friendId=#{friendId}")
    FriendRoomDomain getFriendship(Long userId, Long friendId);
    /**
     * 操作朋友关系， 拉黑或者恢复朋友关系
     * @param userId    用户Id
     * @param friendId 好友Id
     * @param roomStatus 房间状态
     */
    @Update("UPDATE ter_room_friend SET roomStatus = #{roomStatus} " +
            "WHERE userId = #{userId} AND friendId = #{friendId}")
    Integer operateFriendship(Long userId,Long friendId, Integer roomStatus);

    /**
     * 获取指定状态好友列表
     *
     * @param userId        用户uId
     * @param roomStatus    好友列表
     * @return  返回指定状态的好友列表
     *
     */
    @Select("SELECT roomId, userId, friendId, roomName, roomStatus, isTop " +
            "FROM ter_room_friend " +
            "WHERE userId = #{userId} AND roomStatus = #{roomStatus}")
    List<FriendRoomDomain> getFriendPage(Long userId, Integer roomStatus);

    /**
     * 将给定的黑命单列表成员加入白名单
     *
     * @param userId        用户uId
     * @param friendList    黑名单成员Id列表
     */
    Integer pullBackWhitePage(Long userId, List<Long> friendList);
}
