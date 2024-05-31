package com.cabin.ter.adapter;

import cn.hutool.core.lang.Snowflake;
import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.cache.RoomFriendCache;
import com.cabin.ter.chat.domain.*;
import com.cabin.ter.chat.enums.GroupRoleEnum;
import com.cabin.ter.chat.enums.HotFlagEnum;
import com.cabin.ter.chat.enums.RoomTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaoye
 * @date Created in 2024-05-29 15:09
 */
@Component
public class ChatAdapter {
    @Autowired
    private Snowflake snowflake;
    public RoomDomain buildRoom(RoomTypeEnum roomTypeEnum){
        RoomDomain roomDomain = new RoomDomain();
        roomDomain.setType(roomTypeEnum.getType());
        roomDomain.setHotFlag(HotFlagEnum.NOT.getType());
        roomDomain.setId(snowflake.nextId());
        roomDomain.setCreateTime(System.currentTimeMillis());
        return roomDomain;
    }

    public GroupRoomDomain buildGroupRoom(UserDomain userDomain, Long roomId){
        GroupRoomDomain groupRoomDomain = new GroupRoomDomain();
        groupRoomDomain.setRoomId(roomId);
        groupRoomDomain.setRoomName(userDomain.getUserName()+"的群组");
        groupRoomDomain.setRoomAvatar(userDomain.getUserAvatar());
        groupRoomDomain.setCreateTime(System.currentTimeMillis());
        return groupRoomDomain;
    }

    public  List<GroupMemberDomain> buildGroupMemberBatch(List<Long> uidList, Long groupId) {
        return uidList.stream()
                .distinct()
                .map(uid -> {
                    GroupMemberDomain member = new GroupMemberDomain();
                    member.setId(snowflake.nextId());
                    member.setRole(GroupRoleEnum.MEMBER.getType());
                    member.setUid(uid);
                    member.setGroupId(groupId);
                    member.setCreateTime(System.currentTimeMillis());
                    return member;
                }).collect(Collectors.toList());
    }

    /**
     * 获取好友uId 集合
     *
     * @param values
     * @param uid
     * @return
     */
    public Set<Long> getFriendUidSet(Collection<FriendRoomDomain> values, Long uid){
        return values.stream()
                .map(a-> this.getFriendUid(a, uid))
                .collect(Collectors.toSet());
    }

    /**
     * 获取好友uid
     */
    public Long getFriendUid(FriendRoomDomain roomFriend, Long uid) {
        return Objects.equals(uid, roomFriend.getAUId()) ? roomFriend.getBUId() : roomFriend.getAUId();
    }




















}
